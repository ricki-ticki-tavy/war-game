package api.core;

import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.enums.EventType;
import api.geo.Coords;
import api.game.Influencer;
import api.game.action.AttackResult;
import api.game.map.LevelMap;
import api.game.map.Player;
import api.game.map.metadata.GameRules;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Контекст игровой
 */
public interface Context {

  /**
   * Подписаться на событие
   *
   * @param eventTypes
   * @param consumer
   * @return
   */
  String subscribeEvent(Consumer<Event> consumer, EventType... eventTypes);

  /**
   * Отписаться от события
   *
   * @param consumerId
   * @param eventTypes
   * @return
   */
  Result<Context> unsubscribeEvent(String consumerId, EventType... eventTypes);

  /**
   * UUID контекста (сессии)
   *
   * @return
   */
  String getContextId();

  /**
   * Название игры
   *
   * @return
   */
  String getGameName();


  /**
   * Признак публикуется ли игра в списке или доступна только по ссылке
   *
   * @return
   */
  boolean isHidden();

  /**
   * Получить имена игроков, начавших игру. Только для контекста с начатой игрой
   * @return
   */
  Result<List<String>> getFrozenListOfPlayers();

  /**
   * Успех, если текущий статус игры совпадает с переданным
   * @return
   */
  Result<Context> ifGameRan(boolean state);

  /**
   * Успех, если текущий статус игры совпадает с переданным
   * @return
   */
  Result<Context> ifGameDeleting(boolean state);

  /**
   * Проверка допустимости новых координат воина
   * @param warrior
   * @param newCoords
   * @return
   */
  Result<Context> ifNewWarriorSCoordinatesAreAvailable(Warrior warrior, Coords newCoords);

  /**
   * Проверяет возможно ли выполнение атаки или применения способности данным юнитом в этом ходе
   * @param userName
   * @param warriorId
   * @return
   */
  Result<Warrior> ifWarriorCanActsAtThisTurn(String userName, String warriorId);

  /**
   * возвращает текущий статус игры
   * @return
   */
  boolean isGameRan();

  /**
   * Контекст в процессе удаления
   * @return
   */
  boolean isDeleting();

  /**
   * Перевод контекста в состояние начала удаления
   * @return
   */
  Result<Context> initDelete();

  /**
   * Возвращает игровой движок
   *
   * @return
   */
  Core getCore();

  /**
   * Инициировать срабатывание события
   *
   * @param gameEvent
   * @return
   */
  void fireGameEvent(Event gameEvent);

  /**
   * Инициировать срабатывание события
   *
   * @return
   */
  void fireGameEvent(
          Event causeEvent
          , EventType eventType
          , EventDataContainer source
          , Map<String, Object> params);

  /**
   * Получить карту игры со всеми игроками и воинами
   *
   * @return
   */
  LevelMap getLevelMap();

  /**
   * Загрузить карту
   *
   * @param gameRules
   * @param map
   * @return
   */
  Result<Context> loadMap(GameRules gameRules, InputStream map, String gameName, boolean hidden);

  /**
   * Создать и подключить к игре игрока.
   *
   * @param player
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
   * Найти юнит по его коду независимо от того, какому игроку он принадлежит
   * @param warriorId
   * @return
   */
  Result<Warrior> findWarriorById(String warriorId);

  /**
   * Создать воина на карте при начальной расстановке
   *
   * @param baseWarriorClass
   * @return
   */
  Result<Warrior> createWarrior(String userName, String baseWarriorClass, Coords coords);

  /**
   * Атаковать выбранным оружием другого воина
   * @param userName
   * @param attackerWarriorId
   * @param targetWarriorId
   * @param weaponId
   * @return
   */
  Result<AttackResult> attackWarrior(String userName, String attackerWarriorId, String targetWarriorId, String weaponId);

  /**
   * Переместить юнит на новые координаты
   * @param userName
   * @param warriorId
   * @param newCoords
   * @return
   */
  Result<Warrior> moveWarriorTo(String userName, String warriorId, Coords newCoords);

  /**
   * Откатить перемещение воина
   * @return
   */
  Result<Warrior> rollbackMove(String userName, String warriorId);

  /**
   * Возвращает координаты,куда можно переместить перемещаемого юнита, исходя из того, куда его хотят переместить
   * @param userName
   * @param warriorId
   * @param coords
   * @return
   */
  Result<Coords> whatIfMoveWarriorTo(String userName, String warriorId, Coords coords);

  /**
   * Удалить юнит игроком
   * @param userName
   * @param warriorId
   * @return
   */
  Result<Warrior> removeWarrior(String userName, String warriorId);

  /**
   * Вооружить воина предметом
   * @param userName
   * @param warriorId
   * @param weaponClass
   * @return
   */
  Result<Weapon> giveWeaponToWarrior(String userName, String warriorId, Class<? extends Weapon> weaponClass);

  /**
   * Найти оружие по его id
   * @param userName
   * @param warriorId
   * @param weaponId
   * @return
   */
  Result<Weapon> findWeaponById(String userName, String warriorId, String weaponId);

  /**
   * Получить пользователя - создателя игры
   *
   * @return
   */
  Player getContextCreator();

  /**
   * Получить параметры игры
   *
   * @return
   */
  GameRules getGameRules();

  /**
   * Установить признак готовности
   * @param player
   * @param readyToGame
   * @return
   */
  Result<Player> setPlayerReadyToGameState(Player player, boolean readyToGame);

  /**
   * Найти пользователя в контексте
   *
   * @param userName
   * @return
   */
  Result<Player> findUserByName(String userName);

  /**
   * Получить игрока, который сейчас ходит
   *
   * @return
   */
  Result<Player> getPlayerOwnsThisTurn();

  /**
   * Это утверждение, что переданный игрок и является ходящим сейчас
   *
   * @return
   */
  Result<Player> ifPlayerOwnsTheTurnEqualsTo(Player player, String... args);

  /**
   * Передача хода следующему игроку
   * @param userName
   * @return
   */
  Result<Player> nextTurn(String userName);

  /**
   * Получить список оказываемых влияний на юнит
   * @return
   */
  Result<List<Influencer>> getWarriorSInfluencers(String userName, String warriorId);

  /**
   * Получить расстояние в "пикелях" от координаты FROM до  координаты TO.
   * @param from
   * @param to
   * @return
   */
  int calcDistanceTo(Coords from, Coords to);



}
