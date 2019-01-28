package api.game.map;

import api.core.Context;
import api.core.Result;
import api.entity.base.BaseEntityHeader;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.game.Coords;
import api.game.Rectangle;

import java.util.List;

/**
 * Игрок
 */
public interface Player extends BaseEntityHeader{

  /**
   * Добавить в коллекцию воина
   * @param coords
   * @param warriorBaseClassName
   * @return
   */
  Result<Warrior> createWarrior(String warriorBaseClassName, Coords coords);

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
  Result<Context> findContext();

  /**
   * Найти воина по его коду
   * @param warriorId
   * @return
   */
  Result<Warrior> findWarriorById(String warriorId);

  /**
   * Установить статус готовность к игре
   * @param ready
   * @return
   */
  Result<Player> setReadyToPlay(boolean ready);

  /**
   * Получить статус готовности к игре
   * @return
   */
  boolean isReadyToPlay();

  /**
   * Переместить юнит на новые координаты
   * @param warriorId
   * @param newCoords
   * @return
   */
  Result<Warrior> moveWarriorTo(String warriorId, Coords newCoords);

  /**
   * очищает воинов, артефакты и прочее у игрока
   * @return
   */
  Result<Player> clear();

  /**
   * Переместить юнит на новые координаты
   * @param player
   * @param warriorId
   * @param newCoords
   * @return
   */
  Result<Warrior> moveWarriorTo(Player player, String warriorId, Coords newCoords);

  /**
   * Удалить юнит игроком
   * @param warriorId
   * @return
   */
  Result<Warrior> removeWarrior(String warriorId);



}
