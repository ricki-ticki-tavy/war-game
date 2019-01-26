package api.game.map;

import api.core.Context;
import api.core.Result;
import api.entity.base.BaseEntityHeader;
import api.entity.warrior.Warrior;
import api.game.Rectangle;

import java.util.List;

/**
 * Игрок
 */
public interface Player extends BaseEntityHeader{

  /**
   * Добавить в коллекцию воина
   * @param warrior
   * @return
   */
  Result<Warrior> addWarrior(Warrior warrior);

  /**
   *  Вернуть всех воинов
   * @return
   */
  List<Warrior> getWarriors();

  /**
   * Задать зону выставления воинов
   * @param startZone
   */
  void setStartZone(Rectangle startZone);


  /**
   * Получить зону выставления фигурок
   * @return
   */
  Rectangle getStartZone();

  /**
   * Задать игроку новый контекст. При этом из старого контекста должен быть выход
   * @param newContext
   * @return
   */
  Result replaceContext(Context newContext);

  /**
   * Задать игроку новый контекст без проверок и переподключений
   * @param newContext
   * @return
   */
  Result<Player> replaceContextSilent(Context newContext);

  /**
   * Получить контекст игрока
   * @return
   */
  Context getContext();

  /**
   * Найти воина по его коду
   * @param warriorId
   * @return
   */
  Result<Warrior> findWarriorById(String warriorId);

}
