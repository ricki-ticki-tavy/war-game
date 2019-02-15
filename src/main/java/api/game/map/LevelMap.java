package api.game.map;

import api.core.Context;
import api.core.Owner;
import api.core.Result;
import api.entity.stuff.Artifact;
import api.game.ability.Modifier;
import api.game.ability.Influencer;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.enums.LifeTimeUnit;
import api.enums.TargetTypeEnum;
import api.geo.Coords;
import api.geo.Rectangle;
import api.game.action.InfluenceResult;
import api.game.map.metadata.LevelMapMetaDataXml;
import core.game.GameProcessData;

import java.util.List;

/**
 * Игровая карта c игроками и воинами
 */
public interface LevelMap {
  /**
   * Название карты
   * @return
   */
  String getName();

  /**
   * Описание
   * @return
   */
  String getDescription();

  /**
   * Размер базового единичного отрезка в пикселях
   * @return
   */
  int getSimpleUnitSize();

  /**
   * Количество игроков
   * @return
   */
  int getMaxPlayerCount();

  /**
   * ширина карты в базовых единицах измерения
   * @return
   */
  int getWidthInUnits();

  /**
   * высота карты в базовых единицах измерения
   * @return
   */
  int getHeightInUnits();

  /**
   * Вернуть данные, связанные с процессом игры
   * @return
   */
  GameProcessData getGameProcessData();

  /**
   * Выполнить приготовления, связанные с
   */
  void beginGame();

  /**
   * Получить список воинов в окружности заданным радиусом
   * @param center координата центра
   * @param radiusInPixels радиус окружности в "пикселях". 0 - все воины
   * @param warriorsType Тип собираемых воинов. Все, враги, свои
   * @param alliedPlayer если warriorsType не ANY, то игрок, который ищет других воинов. Дружественный игрок
   * @return
   */
  List<Warrior> getWarriors(Coords center, int radiusInPixels, TargetTypeEnum warriorsType, Player alliedPlayer);

  /**
   * Добавить воина в заданные координаты заданному игроку
   * @param player
   * @param warriorBaseClassName
   * @param coords
   * @return
   */
  Result<Warrior> createWarrior(Player player, String warriorBaseClassName, Coords coords);

  /**
   * Переместить юнит на новые координаты
   * @param player
   * @param warriorId
   * @param newCoords
   * @return
   */
  Result<Warrior> moveWarriorTo(Player player, String warriorId, Coords newCoords);

  /**
   * Откатить перемещение воина
   * @return
   */
  Result<Warrior> rollbackMove(Player player, String warriorId);

  /**
   * Возвращает координаты,куда можно переместить перемещаемого юнита, исходя из того, куда его хотят переместить
   * @param player
   * @param warriorId
   * @param coords
   * @return
   */
  Result<? extends Coords> whatIfMoveWarriorTo(Player player,  String warriorId, Coords coords);

  /**
   * Удалить юнит игроком
   * @param player
   * @param warriorId
   * @return
   */
  Result<Warrior> removeWarrior(Player player, String warriorId);

  /**
   * Вооружить воина предметом
   * @param player
   * @param warriorId
   * @param weaponClass
   * @return
   */
  Result<Weapon> giveWeaponToWarrior(Player player, String warriorId, Class<? extends Weapon> weaponClass);

  /**
   * дать воину артефакт. Если такой артефакт такого типа уже есть, то будет отказ
   * @param player        игрок, которому принадлежит воин
   * @param warriorId     код воина, которому дается артефакт
   * @param artifactClass класс даваемого артефакта
   * @return
   */
  Result<Artifact<Warrior>> giveArtifactToWarrior(Player player, String warriorId, Class<? extends Artifact<Warrior>> artifactClass);

  /** выбросить артефакт воина
   *
   * @param player
   * @param artifactInstanceId
   * @param warriorId
   * @return
   */
  Result<Artifact<Warrior>> dropArtifactByWarrior(Player player, String warriorId, String artifactInstanceId);

  /**
   * Бросить оружие
   * @param player
   * @param warriorId
   * @param weaponId
   * @return
   */
  Result<Weapon> dropWeaponByWarrior(Player player, String warriorId, String weaponId);

  /**
   * Найти оружие по его id
   * @param player
   * @param warriorId
   * @param weaponId
   * @return
   */
  Result<Weapon> findWeaponById(Player player, String warriorId, String weaponId);

  /**
   * Найти юнит по его коду независимо от того, какому игроку он принадлежит
   * @param warriorId
   * @return
   */
  Result<Warrior> findWarriorById(String warriorId);

  /**
   * Атаковать выбранным оружием другого воина
   * @param attackerWarriorId
   * @param targetWarriorId
   * @param weaponId
   * @return
   */
  Result<InfluenceResult> attackWarrior(Player player, String attackerWarriorId, String targetWarriorId, String weaponId);

  /**
   * Добавить игрока в игру
   * @return
   */
  Result connectPlayer(Player player);

  /**
   * Отключить пользователя от игры.
   *
   * @param player
   * @return
   */
  Result disconnectPlayer(Player player);

  /**
   * Вернуть список игроков
   * @return
   */
  List<Player> getPlayers();

  /**
   * Вернуть игрока по его коду
   * @param playerId
   * @return
   */
  Player getPlayer(String playerId);

  /**
   * Зоны, в которых игроки могут расставлять фигурки на карте
   */
  List<Rectangle> getPlayerStartZone(String playerId);

  /**
   * Инициализация карты
   */
  void init(Context gameContext, LevelMapMetaDataXml levelMapMetaData);

  /**
   * Признак успешно липодготовлена карта
   * @return
   */
  boolean isLoaded();

  /**
   * Проверка допустимости новых координат воина
   * @param warrior
   * @param newCoords
   * @return
   */
  Result<Context> ifNewWarriorSCoordinatesAreAvailable(Warrior warrior, Coords newCoords);

  /**
   * Возвращает цену за перемещение на единицу длины для данного юнита с учетом всех его влияний, классов брони
   * и прочего
   * @param warrior
   * @return
   */
   int getWarriorSMoveCost(Warrior warrior);

  /**
   * Возвращает координаты для юнита. Для получения координат этой функцией используется таблица юнитов
   * которыми делались действия в этом ходу. Если движение юнита можно откатить, то возвращаются его
   * координаты на которые можно сделать откат, если нельзя откатить или юнита нет в таблице, то берется
   * оригинальное значение координат
   * @param warrior
   * @return
   */
   Result<Coords> getWarriorSOriginCoords(Warrior warrior);

  /**
   * Проверяет возможно ли выполнение атаки или применения способности данным юнитом в этом ходе
   * @param player
   * @param warriorId
   * @return
   */
  Result<Warrior> ifWarriorCanActsAtThisTurn(Player player, String warriorId);

  /**
   * Получить игрока, выполняющего ход в данное время
   * @return
   */
  Result<Player> getPlayerOwnsThisTurn();

  /**
   * Передача хода следующему игроку
   * @param player  сменяемый игрок. Тот, кто передает ход следующему
   * @return
   */
  Result<Player> nextTurn(Player player);

  /**
   * добавить влияние юниту
   * @param modifier
   * @param lifeTimeUnit
   * @param lifeTime
   * @return
   */
  Result<Influencer> addInfluenceToWarrior(Player player, String warriorId, Modifier modifier, Owner source, LifeTimeUnit lifeTimeUnit, int lifeTime);

  /**
   * Получить список оказываемых влияний на юнит
   * @return
   */
  Result<List<Influencer>> getWarriorSInfluencers(Player player, String warriorId);

  /**
   * Попытка перемещения юнита на новую координату
   *
   * @param warrior
   * @param to
   * @param objectSize
   * @param maxWayLengthInPixels - максимальная дальность перемещения в "пикселах"
   * @param perimeter
   * @return
   */
  Result<Coords> tryToMove(Warrior warrior, Coords to, int objectSize, int maxWayLengthInPixels, Rectangle perimeter);

  /**
   * Это утверждение, что переданный игрок и является ходящим сейчас
   *
   * @return
   */
  Result<Player> ifPlayerOwnsThisTurn(Player player, String... args);


}
