package api.game.wraper;

import api.core.Result;
import api.game.map.metadata.GameRules;

/**
 * Класс, собирающий под собой весь функционал игры
 */
public interface GameWraper {
  /**
   * Содание экземпляра авторизованного пользователя
   */
  Result login(String userName);

  /**
   * Создать новую игру
   *
   * @param ownerUserName
   * @param gameRules
   * @param mapName
   * @param gameName
   * @param hidden
   * @return
   */
  Result createGame(String ownerUserName
          , GameRules gameRules
          , String mapName
          , String gameName
          , boolean hidden);

  /**
   * Получить список игровых контекстов
   * @return
   */
  Result getGamesList();
}
