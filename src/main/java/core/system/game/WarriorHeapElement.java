package core.system.game;

import api.entity.warrior.Warrior;
import core.system.ActiveCoords;

/**
 * Класс, используемый для ссылки на юнит, задействованный в данном ходу. Содержит некоторые исходные значения
 * юнита, которые можно восстановить для отмены хода.
 */
public class WarriorHeapElement {
  private Warrior warrior;
  private ActiveCoords originalCoords;
  private boolean moveLocked;
  private boolean rollbackAvailable;

  public WarriorHeapElement(Warrior warrior){
    this.warrior = warrior;
    this.moveLocked = false;
    this.originalCoords = new ActiveCoords(warrior.getCoords());
    this.rollbackAvailable = true;
  }

  /**
   * получить юнит
   * @return
   */
  public Warrior getWarrior() {
    return warrior;
  }

  /**
   * Получить его координаты ДО начала движения в данном ходу
   * @return
   */
  public ActiveCoords getOriginalCoords() {
    return originalCoords;
  }

  /**
   * признак можно ли вернуть юнит на его начальные позиции и отказаться от его использования в данном худе, чтобы
   * выполнить действия другим юнитом
   * @return
   */
  public boolean isMoveLocked() {
    return moveLocked;
  }

  /**
   * Зафиксировать юнит на этом месте и заблокировать дальнейшую возможность движения. Это должно происходить при
   * любом действии юнита, кроме перемещения. Данный признак может, однако, скидываться некоторыми способностями,
   * давая после атаки возможность переместиться
   */
  public void lockMove() {
    this.moveLocked = true;
  }

  /**
   * заблокировать возможность отката перемещения юнита. Это должно происходить при
   * любом действии юнита, кроме перемещения.
   */
  public void lockRollback() {
    this.rollbackAvailable = false;
  }

  /**
   * Указывает можно ли откатить перемещение юнита в данном ходе. Это возможно, пока он не начал применять что-либо кроме
   * перемещения
   * @return
   */
  public boolean isRollbackAvailable() {
    return rollbackAvailable;
  }
}
