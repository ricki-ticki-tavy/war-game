package api.core;

import api.entity.base.BaseEntityHeader;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.enums.EventType;
import api.game.GameEvent;
import api.game.map.LevelMap;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import core.entity.warrior.AbstractBaseWarriorClass;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Контекст игровой
 */
public interface GameContext {

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
  void fireGameEvent(GameEvent gameEvent);

  /**
   * Инициировать срабатывание события
   *
   * @return
   */
  void fireGameEvent(
          GameEvent causeEvent
          , EventType eventType
          , BaseEntityHeader source
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
  void loadMap(String userGameCreator, GameRules gameRules, InputStream map, String gameName, boolean hidden);

  /**
   * Создать и подключить к игре игрока.
   *
   * @param playerName
   * @param playerSessionId
   * @return
   */
  Player connectPlayer(String playerName, String playerSessionId);

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
  String getUserGameCreator();

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
  String subscribeEvent(List<EventType> eventTypes, Consumer<GameEvent> consumer);

}
