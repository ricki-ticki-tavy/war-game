package api.core;

import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.enums.EventType;
import api.game.Coords;
import api.game.Event;
import api.game.EventDataContainer;
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
   * Создать воина на карте при начальной расстановке
   *
   * @param baseWarriorClass
   * @return
   */
  Result<Warrior> createWarrior(String userName, String baseWarriorClass, Coords coords);

  /**
   * Переместить юнит на новые координаты
   * @param userName
   * @param warriorId
   * @param newCoords
   * @return
   */
  Result<Warrior> moveWarriorTo(String userName, String warriorId, Coords newCoords);

  /**
   * Удалить юнит игроком
   * @param userName
   * @param warriorId
   * @return
   */
  Result<Warrior> removeWarrior(String userName, String warriorId);

  /**
   * Получить пользователя - создателя игры
   *
   * @return
   */
  Player getContextOwner();

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
  Result<Player> getPlayerOwnsTheTurn();


  /**
   * проверяет может ли ходящий сейчас игрок переместить данный юнит. Проверка права игрока ходить, контексты и прочее
   * перед этим вызовом уже должно быть проверено.
   *
   * @param warrior
   */
  Result<Warrior> ifUnitCanMove(Warrior warrior);


}
