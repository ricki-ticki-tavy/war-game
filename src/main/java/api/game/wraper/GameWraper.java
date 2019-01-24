package api.game.wraper;

import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.game.Coords;
import api.game.map.Player;
import api.game.map.metadata.GameRules;

import java.util.List;

/**
 * Класс, собирающий под собой весь функционал игры
 */
public interface GameWraper {
  /**
   * Содание экземпляра авторизованного пользователя
   */
  Result<Player> login(String userName);

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
  Result<Warrior> createWarrior(String userName, String className, Coords coords);

  /**
   * Вооружить воина предметом
   * @param userName
   * @param warriorId
   * @param weaponName
   * @return
   */
  Result<Weapon> giveWeaponToWarrior(String userName, String warriorId, String weaponName);

}
