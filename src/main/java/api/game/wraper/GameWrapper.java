package api.game.wraper;

import api.core.Context;
import api.core.Core;
import api.core.Result;
import api.entity.warrior.Influencer;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.game.Coords;
import api.game.map.Player;
import api.game.map.metadata.GameRules;

import java.util.List;

/**
 * Класс, собирающий под собой весь функционал игры
 */
public interface GameWrapper {
  /**
   * Содание экземпляра авторизованного пользователя
   */
  Result<Player> login(String userName);

  /**
   * Подключиться к игре
   * @param userName
   * @param contextId
   * @return
   */
  Result<Player> connectToGame(String userName, String contextId);

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
  Result<Context> createGame(String ownerUserName
          , GameRules gameRules
          , String mapName
          , String gameName
          , boolean hidden);


  /**
   * Получить список игровых контекстов
   * @return
   */
  Result<List<Context>> getGamesList();

  /**
   * Добавить воина игроку
   * @param userName
   * @param className
   * @return
   */
  Result<Warrior> createWarrior(String contextId, String userName, String className, Coords coords);

  /**
   * Переместить юнит на заданные координаты
   * @param userName
   * @param warriorId
   * @param coords
   * @return
   */
  Result<Warrior> moveWarriorTo(String contextId, String userName, String warriorId, Coords coords);

  /**
   * Откатить перемещение воина
   * @return
   */
  Result<Warrior> rollbackMove(String contextId, String userName, String warriorId);

  /**
   * Возвращает координаты,куда можно переместить перемещаемого юнита, исходя из того, куда его хотят переместить
   * @param contextId
   * @param userName
   * @param warriorId
   * @param coords
   * @return
   */
  Result<Coords> whatIfMoveWarriorTo(String contextId, String userName, String warriorId, Coords coords);

  /**
   * Вооружить воина предметом
   * @param userName
   * @param warriorId
   * @param weaponName
   * @return
   */
  Result<Weapon> giveWeaponToWarrior(String userName, String warriorId, String weaponName);

  // TODO переделать с кодом контекста в параметр
  /**
   * Забрать предмет у воина
   * @param userName
   * @param warriorId
   * @param weaponId
   * @return
   */
  Result<Weapon> takeWeaponFromWarrior(String userName, String warriorId, String weaponId);

  /**
   * Удалить юнит игроком
   * @param contextId
   * @param userName
   * @param warriorId
   * @return
   */
  Result<Warrior> removeWarrior(String contextId, String userName, String warriorId);

  /**
   * Уведомить, что игрок закончил расстановку и готов к игре
   * @param userName
   * @return
   */
  Result<Player> playerReady(String userName, boolean readyToPlay);

  /**
   * Возвращает игрока, которому сейчас принадлежит ход
   * @return
   */
  Result<Player> getGetPlayerOwnsTheRound(String contextId);

  /**
   * Возвращает имена базовых классов воинов
   * @return
   */
  Result<List<String>> getBaseWarriorClasses();

  /**
   * Возвращает имена базовых классов вооружения
   * @return
   */
  Result<List<String>> getWeaponClasses();

  /**
   * Передача хода следующему игроку
   * @param contextId
   * @param userName
   * @return
   */
  Result<Player> nextTurn(String contextId, String userName);

  /**
   * Получить список оказываемых влияний на юнит
   * @return
   */
  Result<List<Influencer>> getWarriorSInfluencers(String contextId, String userName, String warriorId);


  Core getCore();
}
