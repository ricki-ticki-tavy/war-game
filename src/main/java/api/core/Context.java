package api.core;

import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.game.GameEvent;
import api.game.map.LevelMap;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import core.entity.warrior.AbstractBaseWarriorClass;

import java.io.InputStream;

/**
 * Контекст игровой
 */
public interface Context {

  /**
   * UUID контекста (сессии)
   * @return
   */
  String getContextId();

  /**
   * Возвращает игровой движок
   * @return
   */
  Core getCore();

  /**
   * Инициировать срабатывание события
   * @param gameEvent
   * @return
   */
  boolean fireGameEvent(GameEvent gameEvent);

  /**
   * Получить карту игры со всеми игроками и воинами
   * @return
   */
  LevelMap getLevelMap();

  /**
   * Загрузить карту
   * @param userGameCreator
   * @param gameRules
   * @param map
   * @return
   */
  void loadMap(String userGameCreator, GameRules gameRules, InputStream map);

  /**
   * Создать и подключить к игре игрока.
   * @param playerSessionId
   * @return
   */
  Player connectPlayer(String playerSessionId);

  /**
   * Создать воина на карте
   * @param baseWarriorClass
   * @return
   */
  Warrior createWarrior(String playerId, Class<? extends WarriorBaseClass> baseWarriorClass);



  /**
   * Получить пользователя - создателя игры
   * @return
   */
  String getUserGameCreator();

  /**
   * Получить параметры игры
   * @return
   */
  GameRules getGameRules();
}
