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
  Result<Context> ifDeleting(boolean state);

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
   * Создать воина на карте
   *
   * @param baseWarriorClass
   * @return
   */
  Result<Warrior> createWarrior(Player player, Class<? extends WarriorBaseClass> baseWarriorClass, Coords coords);


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

}
