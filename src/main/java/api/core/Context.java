package api.core;

import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.enums.EventType;
import api.game.Event;
import api.game.EventDataContainer;
import api.game.map.LevelMap;
import api.game.map.Player;
import api.game.map.metadata.GameRules;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Контекст игровой
 */
public interface Context {

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
   * @param userGameCreator
   * @param gameRules
   * @param map
   * @return
   */
  Result loadMap(Player userGameCreator, GameRules gameRules, InputStream map, String gameName, boolean hidden);

  /**
   * Создать и подключить к игре игрока.
   *
   * @param player
   * @param playerSessionId
   * @return
   */
  Result connectPlayer(Player player, String playerSessionId);

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
  Warrior createWarrior(String playerId, Class<? extends WarriorBaseClass> baseWarriorClass);


  /**
   * Получить пользователя - создателя игры
   *
   * @return
   */
  Player getUserGameCreator();

  /**
   * Получить параметры игры
   *
   * @return
   */
  GameRules getGameRules();

  /**
   * Подписаться на событие
   *
   * @param eventTypes
   * @param consumer
   * @return
   */
  String subscribeEvent(Consumer<Event> consumer, EventType... eventTypes);

}
