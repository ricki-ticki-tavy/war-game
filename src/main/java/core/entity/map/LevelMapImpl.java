package core.entity.map;

import api.core.Context;
import api.core.Result;
import api.entity.ability.Modifier;
import api.entity.warrior.Influencer;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.enums.LifeTimeUnit;
import api.enums.TargetTypeEnum;
import api.game.Coords;
import api.game.EventDataContainer;
import api.game.Rectangle;
import api.game.action.AttackResult;
import api.game.map.LevelMap;
import api.game.map.Player;
import api.game.map.metadata.LevelMapMetaDataXml;
import core.game.GameProcessData;
import core.system.ResultImpl;
import core.system.error.GameErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static api.enums.EventType.*;
import static api.enums.TargetTypeEnum.ANY;
import static core.system.error.GameErrors.*;

/**
 * Игровая карта
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LevelMapImpl implements LevelMap {

  private static final Logger logger = LoggerFactory.getLogger(LevelMap.class);

  private String name;
  private String description;
  private int simpleUnitSize;
  private int width;
  private int height;
  private int maxPlayersCount;
  private List<Rectangle> playerStartZones;
  private Map<String, Player> players;
  private Context context;
  private boolean loaded = false;
  private Rectangle mapBorder;

  private GameProcessData gameProcessData;

  @Autowired
  BeanFactory beanFactory;

  //===================================================================================================
  //===================================================================================================

  public LevelMapImpl() {
    gameProcessData = new GameProcessData();
  }
  //===================================================================================================

  @Override
  public void beginGame() {
    // границы движения - вся карта
    mapBorder = new Rectangle(new Coords(5, 5), new Coords(width - 5, height - 5));

    // сохраним список пользователей, начавших игру
    getPlayers().stream()
            .forEach(player -> {
              gameProcessData.frozenListOfPlayers.put(gameProcessData.frozenListOfPlayers.size(), player);
              // всех подготовим к защите
              player.prepareToDefensePhase();
            });
    // первого игрока готовим к атаке
    getPlayerOwnsThisTurn()
            .map(player -> player.prepareToAttackPhase());
  }
  //===================================================================================================

  @Override
  public GameProcessData getGameProcessData() {
    return gameProcessData;
  }
  //===================================================================================================

  @Override
  public String getName() {
    return name;
  }
  //===================================================================================================

  @Override
  public String getDescription() {
    return description;
  }
  //===================================================================================================

  @Override
  public void init(Context gameContext, LevelMapMetaDataXml levelMapMetaData) {
    logger.info("map initializing started \"" + levelMapMetaData.name + "\" in context " + gameContext.getContextId());
    this.context = gameContext;
    this.name = levelMapMetaData.name;
    this.description = levelMapMetaData.description;
    this.simpleUnitSize = levelMapMetaData.simpleUnitSize;
    this.width = levelMapMetaData.width * simpleUnitSize;
    this.height = levelMapMetaData.height * simpleUnitSize;
    this.maxPlayersCount = levelMapMetaData.maxPlayersCount;
    playerStartZones = new LinkedList();

    levelMapMetaData.playerStartZones.stream().forEach(rectangle ->
            playerStartZones.add(new Rectangle(new Coords(rectangle.topLeftConner.x * simpleUnitSize, rectangle.topLeftConner.y * simpleUnitSize)
                    , new Coords(rectangle.bottomRightConner.x * simpleUnitSize, rectangle.bottomRightConner.y * simpleUnitSize)))
    );

    players = new ConcurrentHashMap<>(maxPlayersCount);

    loaded = true;
    logger.info("map initializing succeed \"" + levelMapMetaData.name + "\" in context " + gameContext.getContextId());
  }
  //===================================================================================================

  @Override
  public List<Rectangle> getPlayerStartZone(String playerId) {
    return playerStartZones;
  }
  //===================================================================================================

  @Override
  public int getMaxPlayerCount() {
    return maxPlayersCount;
  }
  //===================================================================================================

  @Override
  public int getWidthInUnits() {
    return width;
  }
  //===================================================================================================

  @Override
  public int getHeightInUnits() {
    return height;
  }
  //===================================================================================================

  @Override
  public List<Warrior> getWarriors(Coords center, int radius, TargetTypeEnum warriorsType, Player alliedPlayer) {
    return new ArrayList<>(innerGetAllWarriorsOnMap(center, radius, warriorsType, alliedPlayer).values());
  }
  //===================================================================================================

  @Override
  public Result<Warrior> createWarrior(Player player, String warriorBaseClassName, Coords coords) {
    if (player.getWarriors().size() >= context.getGameRules().getMaxStartCreaturePerPlayer()) {
      return ResultImpl.fail(GameErrors.PLAYER_UNITS_LIMIT_EXCEEDED.getError(player.getTitle()
              , String.valueOf(context.getGameRules().getMaxStartCreaturePerPlayer())));
    }
    // TODO сделать проверку координат
    return player.createWarrior(warriorBaseClassName, coords);
  }
  //===================================================================================================

  @Override
  public Result connectPlayer(Player player) {
    Result result = null;
    if (players.containsKey(player.getId())) {
      // игрок есть.
      result = ResultImpl.success(player);
      // проверим, что он подключился к тому же контексту, что и ранее, если ранее уже было подключение к контексту
      player.findContext()
              // контексты не совпали
              .map(oldContext -> oldContext != context
                      ? player.replaceContext(context)
                      : ResultImpl.success(context));
      context.fireGameEvent(null, PLAYER_RECONNECTED, new EventDataContainer(player, result), null);
    } else {
      if (maxPlayersCount >= players.size()) {
        player.clear();
        if ((result = player.replaceContext(context)).isSuccess()) {
          players.put(player.getId(), player);
          player.setStartZone(playerStartZones.get(players.size() - 1));
          result = ResultImpl.success(player);
        }
      } else {
        result = ResultImpl.fail(USER_CONNECT_TO_CONTEXT_TOO_MANY_USERS.getError());
      }
      context.fireGameEvent(null, PLAYER_CONNECTED, new EventDataContainer(player, result), null);
    }
    return result;
  }
  //===================================================================================================

  @Override
  public Result disconnectPlayer(Player player) {
    Result result;
    if (players.containsKey(player.getId())) {
      players.remove(player.getId());
      player.replaceContextSilent(null);
      result = ResultImpl.success(player);
      context.fireGameEvent(null, PLAYER_DISCONNECTED, new EventDataContainer(player, result), null);

      // если это создатель игры, и контекст УЖЕ не в режиме удаления, то
      if (!context.isDeleting() && context.getContextCreator().equals(player)) {
        // выкидываем всех игроков
        players.values().stream().forEach(this::disconnectPlayer);
        // Удаляем контекст
        context.getCore().removeGameContext(context.getContextId());
      }
    } else {
      result = ResultImpl.fail(USER_DISCONNECT_NOT_CONNECTED.getError());
      context.fireGameEvent(null, PLAYER_DISCONNECTED, new EventDataContainer(player, result), null);
    }
    return result;
  }
  //===================================================================================================

  @Override
  public List<Player> getPlayers() {
    return new LinkedList(players.values());
  }
  //===================================================================================================

  @Override
  public int getSimpleUnitSize() {
    return simpleUnitSize;
  }
  //===================================================================================================

  @Override
  public Player getPlayer(String playerId) {
    return players.get(playerId);
  }
  //===================================================================================================

  @Override
  public boolean isLoaded() {
    return loaded;
  }
  //===================================================================================================

  @Override
  public Result<Warrior> removeWarrior(Player player, String warriorId) {
    return player.removeWarrior(warriorId);
  }
  //===================================================================================================

  @Override
  public Result<Weapon> giveWeaponToWarrior(Player player, String warriorId, Class<? extends Weapon> weaponClass) {
    return player.giveWeaponToWarrior(warriorId, weaponClass);
  }
  //===================================================================================================

  @Override
  public Result<Weapon> findWeaponById(Player player, String warriorId, String weaponId) {
    return player.findWeaponById(warriorId, weaponId);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> findWarriorById(String warriorId) {
    Optional<Optional<Warrior>> warriorOptional = players.values().stream()
            .map(player -> player.getOriginWarriors().values().stream()
                    .parallel()
                    .filter(nextWarrior -> nextWarrior.getId().equals(warriorId)).findFirst())
            .filter(nextWarriorOptional -> nextWarriorOptional.isPresent()).findFirst();

    return (warriorOptional == null) || (!warriorOptional.isPresent())
            // "Игра %s (id %s) не имеет воина с id %s"
            ? ResultImpl.fail(WARRIOR_NOT_FOUND_ON_THE_MAP.getError(context.getGameName()
            , context.getContextId()
            , warriorId))
            : ResultImpl.success(warriorOptional.get().get());
  }
  //===================================================================================================

  @Override
  public Result<AttackResult> attackWarrior(Player player, String attackerWarriorId, String targetWarriorId, String weaponId) {
    // активна игра, а не режим расстановки
    return context.ifGameRan(true)
            // ход именно этого игрока
            .map(fineContext -> fineContext.ifPlayerOwnsTheTurnEqualsTo(player, String.format(" атаковать воином %s воина %s", attackerWarriorId, targetWarriorId))
                    .map(finePlayer -> finePlayer.attackWarrior(attackerWarriorId, targetWarriorId, weaponId)));
  }
  //===================================================================================================

  protected Result<Warrior> innerMoveWarriorTo(Player player, String warriorId, Coords newCoords) {
    return player.findWarriorById(warriorId)
            .map(warrior -> player.ifWarriorCanMoveAtThisTurn(warrior))
            .map(warrior -> innerWhatIfMoveWarriorTo(player, warrior, newCoords)
                    .map(coords -> {
                      // кинем сообщение, что юнит перемещен
                      Result<Warrior> result = player.moveWarriorTo(warrior, coords);
                      context.fireGameEvent(null, WARRIOR_MOVED
                              , new EventDataContainer(warrior, result), null);
                      return result;
                    }));
  }
  //===================================================================================================

  @Override
  public Result<Warrior> moveWarriorTo(Player player, String warriorId, Coords newCoords) {
    return context.isGameRan()
            ? ifPlayerOwnsThisTurn(player, ": перемещение юнита " + warriorId)
            .map(playerOwnsThisTurn -> innerMoveWarriorTo(playerOwnsThisTurn, warriorId, newCoords))
            : innerMoveWarriorTo(player, warriorId, newCoords);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> rollbackMove(Player player, String warriorId) {
    return ifPlayerOwnsThisTurn(player, ": отмена перемещения юнита " + warriorId)
            .map(playerOwnsThisTurn -> playerOwnsThisTurn.rollbackMove(warriorId));
  }
  //===================================================================================================

  @Override
  public Result<Coords> whatIfMoveWarriorTo(Player player, String warriorId, Coords coords) {
    return player.findWarriorById(warriorId)
            .map(warrior -> innerWhatIfMoveWarriorTo(player, warrior, coords));
  }
  //===================================================================================================

  // TODO ПЕРЕДЕЛАТЬ по слоям. Метод уже есть у юнита!!!
  public Result<Coords> getWarriorSOriginCoords(Warrior warrior) {
    // ищем в списке юнитов, задействованных ы этом ходу наш юнит
    return !warrior.isTouchedAtThisTurn() || warrior.isMoveLocked() || !warrior.isRollbackAvailable()
            ? ResultImpl.success(new Coords(warrior.getCoords()))
            : ResultImpl.success(warrior.getOriginalCoords());
  }
  //===================================================================================================

  @Override
  public Result<Warrior> ifWarriorCanActsAtThisTurn(Player player, String warriorId) {
    return player.findWarriorById(warriorId)
            .map(warrior -> player.ifWarriorCanActsAtThisTurn(warrior));
  }
  //===================================================================================================

  @Override
  public Result<Influencer> addInfluenceToWarrior(Player player, String warriorId, Modifier modifier, Object source, LifeTimeUnit lifeTimeUnit, int lifeTime) {
    return player.addInfluenceToWarrior(warriorId, modifier, source, lifeTimeUnit, lifeTime);
  }
  //===================================================================================================

  //TODO переделать по слоям
  @Override
  public Result<List<Influencer>> getWarriorSInfluencers(Player player, String warriorId) {
    return player.findWarriorById(warriorId)
            .map(warrior -> warrior.getWarriorSInfluencers());
  }
  //===================================================================================================


  public Result<Player> getPlayerOwnsThisTurn() {
    return ResultImpl.success(gameProcessData.frozenListOfPlayers.get(gameProcessData.indexOfPlayerOwnsTheTurn.get()));
  }//===================================================================================================

  /**
   * Сделать следующего игрока по кругу текущим владельцем хода
   *
   * @return
   */
  private Result<Player> switchToNextPlayerTurn() {
    if (gameProcessData.indexOfPlayerOwnsTheTurn.incrementAndGet() >= gameProcessData.frozenListOfPlayers.size()) {
      gameProcessData.indexOfPlayerOwnsTheTurn.set(0);
      context.fireGameEvent(null, ROUND_FULL, new EventDataContainer(this), null);
    }
    return getPlayerOwnsThisTurn();
  }
//===================================================================================================

  @Override
  public Result<Player> nextTurn(Player player) {
    return
            // проверим этот ли игрок сейчас владеет ходом
            ifPlayerOwnsThisTurn(player, ". Передача хода невозможна так как ходит игрок " + getPlayerOwnsThisTurn().getResult().getId())
                    // переведем в защиту уходящего игрока
                    .map(finePlayer -> finePlayer.prepareToDefensePhase())
                    // сменим игрока
                    .map(oldPlayer -> switchToNextPlayerTurn())
                    .map(newPlayer -> {
                      // подготовим нового игрока к ходу
                      return newPlayer.prepareToAttackPhase();
                    });
  }
  //===================================================================================================

  @Override
  public int getWarriorSMoveCost(Warrior warrior) {
    return warrior.getWarriorSMoveCost();
  }
  //===================================================================================================

  public Map<String, Warrior> innerGetAllWarriorsOnMap(Coords from, int radiusInMapUnits, TargetTypeEnum warriorsType, Player alliedPlayer) {
    final Map<String, Warrior> allWarriors;
    int radiusInPixels = radiusInMapUnits * simpleUnitSize;
    // в зависимости от типа искомых юнитов будем готовить разный поток пользователей для сбора воинов
    Stream<Player> sourcePlayersStream = null;
    switch (warriorsType) {
      case ANY:
        sourcePlayersStream = players.values().stream();
        break;
      case ENEMY_WARRIOR:
        sourcePlayersStream = players.values().stream().filter(player -> player != alliedPlayer);
        break;
      case ALLIED_WARRIOR:
        sourcePlayersStream = players.values().stream().filter(player -> player == alliedPlayer);
        break;
      default:
        // "Недопустимое значение параметра %s (%s). %s"
        throw SYSTEM_BAD_PARAMETER.getError("warriorsType", warriorsType == null ? "null" : warriorsType.getCaption(),
                "Допустимы: ANY, ENEMY_WARRIOR, ALLIED_WARRIOR");
    }
    if (from == null) {
      allWarriors = new HashMap<>(100);
      sourcePlayersStream.forEach(player -> allWarriors.putAll(player.getOriginWarriors()));
    } else {
      allWarriors = new ConcurrentHashMap<>(100);
      // сбор юнитов по радиусу от точки
      sourcePlayersStream
              .forEach(player -> player.getOriginWarriors().values().stream()
                      .parallel()
                      .filter(warrior -> context.calcDistanceTo(from, warrior.getTranslatedToGameCoords()) <= radiusInPixels)
                      .forEach(warrior -> allWarriors.put(warrior.getId(), warrior)));
    }
    return allWarriors;
  }
  //===================================================================================================

  private int calcQuadOfWayLength(Coords pointFrom, Coords pointTo) {
    return (pointFrom.getX() - pointTo.getX()) * (pointFrom.getX() - pointTo.getX())
            + (pointFrom.getY() - pointTo.getY()) * (pointFrom.getY() - pointTo.getY());
  }
  //===================================================================================================

  /**
   * Возвращает допустимые координаты, исходя из заградительного периметра и вектора направленя
   * от текущей координаты к новой
   *
   * @param from
   * @param to
   * @param perimeter
   * @return
   */
  public Result<Coords> tryMoveToWithPerimeter(Coords from, Coords to, Rectangle perimeter) {
    BigDecimal functionYX = to.getX() == from.getX()
            ? BigDecimal.ZERO
            : new BigDecimal(to.getY() - from.getY()).divide(
            new BigDecimal(to.getX() - from.getX()), 5, RoundingMode.HALF_UP);
    BigDecimal reverseFunctionXY = to.getY() == from.getY()
            ? BigDecimal.ZERO
            : new BigDecimal(to.getX() - from.getX()).divide(
            new BigDecimal(to.getY() - from.getY()), 5, RoundingMode.HALF_UP);
    int newX = to.getX();
    int newY = to.getY();

    if (perimeter.getTopLeftConner().getX() > newX) {
      // точка левее левого горизонта
      newY += functionYX.multiply(new BigDecimal(perimeter.getTopLeftConner().getX() - newX)).intValue();
      newX = perimeter.getTopLeftConner().getX();
    } else if (perimeter.getBottomRightConner().getX() < newX) {
      // точка правее правого горионта
      newY += functionYX.multiply(new BigDecimal(perimeter.getBottomRightConner().getX() - newX)).intValue();
      newX = perimeter.getBottomRightConner().getX();
    }

    if (perimeter.getTopLeftConner().getY() > newY) {
      // точка выше верхнего горизонта. Опустим ее до верхнего гоизонта и скорректируем ось Y
      newX += reverseFunctionXY.multiply(new BigDecimal(perimeter.getTopLeftConner().getY() - newY)).intValue();
      newY = perimeter.getTopLeftConner().getY();
    } else if (perimeter.getBottomRightConner().getY() < newY) {
      // точка ниже нижнего горионта
      newX += reverseFunctionXY.multiply(new BigDecimal(perimeter.getBottomRightConner().getY() - newY)).intValue();
      newY = perimeter.getBottomRightConner().getY();
    }

    return ResultImpl.success(new Coords(newX, newY));
  }
  //===================================================================================================

  public Result<Coords> tryToMove(Warrior warrior, Coords to, int objectSize, int maxWayLengthInPixels, Rectangle perimeter) {
    // если еще идет расстановка, то координаты юнита и есть его оригинальные координаты
    Coords from = warrior.getTranslatedToGameCoords();
    // сначала проверим ограничение по дальности
    int pwrVectorLength = (to.getY() - from.getY()) * (to.getY() - from.getY())
            + (to.getX() - from.getX()) * (to.getX() - from.getX());
    if ((maxWayLengthInPixels * maxWayLengthInPixels) < pwrVectorLength) {
      // такое расстояние недопустимо. уменьшим его, сохранив вектор
      double coef = Math.sqrt(pwrVectorLength) / maxWayLengthInPixels;
      to = new Coords((int) (from.getX() + (to.getX() - from.getX()) / coef)
              , (int) (from.getY() + (to.getY() - from.getY()) / coef));
    }
    if (perimeter != null) {
      to = tryMoveToWithPerimeter(from, to, perimeter).getResult();
    }

    // проверим нет ли перекрытия
    final int pwrObjectSize = objectSize * objectSize;
    final Coords copyOfTo = new Coords(to);
    Coords to_ = new Coords(to);
    return innerGetAllWarriorsOnMap(null, 0, ANY, null).values().stream()
            .filter(nextWarrior -> warrior != nextWarrior && pwrObjectSize > calcQuadOfWayLength(nextWarrior.getCoords(), copyOfTo))
            .findFirst()
            .map(foundWarrior -> ResultImpl.fail(WARRIOR_CAN_T_MOVE_TO_THIS_POINT.getError(
                    context.getGameName()
                    , context.getContextId()
                    , warrior.getTitle()
                    , warrior.getWarriorBaseClass().getTitle()
                    , warrior.getId()
                    , warrior.getOwner().getId()
                    , to_.toString()
            )))
            .orElse(ResultImpl.success(copyOfTo));
  }
  //===================================================================================================

  private Result<Coords> innerWhatIfMoveWarriorTo(Player player, Warrior warrior, Coords coords) {
    // Если игра началась
    return tryToMove(warrior, coords
            // размер юнита в "пикселях"
            , context.getGameRules().getWarriorSize()
            // очки действия, оставшиеся в данном ходу для перемещения
            , context.isGameRan() ? warrior.getWarriorSActionPoints(true)
                    // делим на стоимость
                    * simpleUnitSize / warrior.getWarriorSMoveCost()
                    : 10000000
            , context.isGameRan() ? mapBorder : player.getStartZone());
  }
  //===================================================================================================

  // TODO not implemented. !!!!!!!   correct it
  @Override
  public Result<Context> ifNewWarriorSCoordinatesAreAvailable(Warrior warrior, Coords newCoords) {
    return ResultImpl.success(this);
  }
  //===================================================================================================

  @Override
  public Result<Player> ifPlayerOwnsThisTurn(Player player, String... args) {
    return getPlayerOwnsThisTurn()
            .map(player1 -> player1 == player
                    ? ResultImpl.success(player)
                    : ResultImpl.fail(PLAYER_IS_NOT_OWENER_OF_THIS_ROUND.getError(
                    player.findContext().getResult().getGameName()
                    , player.findContext().getResult().getContextId()
                    , player.getId()
                    , args.length > 0 ? args[0] : "")));
  }
  //===================================================================================================


}