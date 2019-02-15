package api.entity.warrior;

import api.core.Context;
import api.core.Owner;
import api.core.Result;
import api.entity.stuff.Artifact;
import api.entity.weapon.Weapon;
import api.enums.LifeTimeUnit;
import api.game.ability.Influencer;
import api.game.ability.Ability;
import api.game.ability.Modifier;
import api.game.action.InfluenceResult;
import api.game.map.Player;
import api.geo.Coords;

import java.util.List;
import java.util.Map;

/**
 * Класс воина на карте
 */
public interface Warrior extends Owner, HasCoordinates {
  /**
   * Получить базовый класс
   *
   * @return
   */
  WarriorBaseClass getWarriorBaseClass();

  /**
   * Призванный воин
   *
   * @return
   */
  boolean isSummoned();

  /**
   * Возвращает руки воина со всем снаряжением в них
   *
   * @return
   */
  List<WarriorSHand> getHands();

  /**
   * Перемещает юнит в заданные координаты
   *
   * @param to
   */
  Result<Warrior> moveWarriorTo(Coords to);

  /**
   * Взять в руку оружие
   *
   * @param weaponClass
   * @return
   */
  Result<Weapon> takeWeapon(Class<? extends Weapon> weaponClass);

  /**
   * Бросить оружие. Передается id экземпляра оружия, которое надо бросить
   *
   * @param weaponInstanceId
   * @return
   */
  Result<Weapon> dropWeapon(String weaponInstanceId);

  /**
   * Получить оружие юнита
   *
   * @return
   */
  List<Weapon> getWeapons();

  /**
   * Найти оружие по его id
   * @param weaponId
   * @return
   */
  Result<Weapon> findWeaponById(String weaponId);

  /**
   * Получить значения атрибутов этого юнита
   *
   * @return
   */
  WarriorSBaseAttributes getAttributes();

  /**
   * Подготовка воина перед ходом игрока. Восстановление различных параметров до нормальных значений
   *
   * @return
   */
  Result<Warrior> prepareToAttackPhase();

  /**
   * Подготовка параметров юнита к фазе защиты. То есть когда ход игрока-владельца юнита закончен и ход переходит
   * к следующему игроку
   *
   * @return
   */
  Result<Warrior> prepareToDefensePhase();

  /**
   * Атаковать выбранным оружием другого воина
   * @param targetWarrior
   * @param weaponId
   * @return
   */
  Result<InfluenceResult> attackWarrior(Warrior targetWarrior, String weaponId);

  /**
   * Этот метод вызывается когда воин игрока атакуется. В этом методе происходит анализ всех нанесенныхз уронов,
   * перерасчет их (в случае наличия сопротивления, брони и прочее)
   * @param attackResult
   * @return
   */
  Result<InfluenceResult> defenceWarrior(InfluenceResult attackResult);

  /**
   * добавить влияние юниту
   *
   * @param modifier
   * @param lifeTimeUnit
   * @param lifeTime
   * @return
   */
  Result<Influencer> addInfluenceToWarrior(Modifier modifier, Owner source, LifeTimeUnit lifeTimeUnit, int lifeTime);

  /**
   * добавить влияние юниту
   *
   * @param influencer
   * @return
   */
  Result<Influencer> addInfluenceToWarrior(Influencer influencer);

  /**
   * Получить список оказываемых влияний на юнит
   *
   * @return
   */
  Result<List<Influencer>> getWarriorSInfluencers();

  /**
   * Удалить влияние у юнита
   *
   * @param influencer
   * @return
   */
  Result<Influencer> removeInfluencerFromWarrior(Influencer influencer, boolean silent);

  /**
   * Возвращает количество оставшихся очков действия у юнита. Для получения этого значение функцией используется
   * таблица юнитов которыми делались действия в этом ходу. Если движение юнита можно откатить, то возвращается
   * кол-во очков из самого юнита, а если откат невозможен, то из очков действия, оставшихся у юнита вычитаются
   * очки, затраченные на передвижение и хранящиеся в таблице юнитов, которыми выполнялись действия.
   * @param forMove
   * @return
   */
  int getWarriorSActionPoints(boolean forMove);

  /**
   * Получить его координаты ДО начала движения в данном ходу
   * @return
   */
   Coords getOriginalCoords();

  /**
   * Получить приведенные к игре координаты. Если юнит НЕ перемещался или перемещался, но есть возможность отката
   * перемещения, то вернутся координаты, которые были до начала его движения (originCoords). Если юнит нет
   * возможности отката, то вернутся его координаты текущие
   * @return
   */
   Coords getTranslatedToGameCoords();


  int calcMoveCost(Coords to);

    /**
     * Возвращает цену за перемещение на единицу длины для данного юнита с учетом всех его влияний, классов брони
     * и прочего
     * @return
     */
  int getWarriorSMoveCost();

  /**
   * признак можно ли вернуть юнит на его начальные позиции и отказаться от его использования в данном худе, чтобы
   * выполнить действия другим юнитом
   * @return
   */
  boolean isMoveLocked();

  /**
   * Зафиксировать юнит на этом месте и заблокировать дальнейшую возможность движения. Это должно происходить при
   * любом действии юнита, кроме перемещения. Данный признак может, однако, скидываться некоторыми способностями,
   * давая после атаки возможность переместиться
   */
  void lockMove();

  /**
   * заблокировать возможность отката перемещения юнита. Это должно происходить при
   * любом действии юнита, кроме перемещения.
   */
  void lockRollback();

  /**
   * Откатить перемещение воина
   * @return
   */
  Result<Warrior> rollbackMove();

  /**
   * Установить или снять отметку, что юнит использован в этом ходе
   * @param selectedAtThisTurn
   * @return
   */
  Warrior setTouchedOnThisTurn(boolean selectedAtThisTurn);

  /**
   * возвращает признак был ли юнит использован в этом ходе
   * @return
   */
  boolean isTouchedAtThisTurn();

  /**
   * Указывает можно ли откатить перемещение юнита в данном ходе. Это возможно, пока он не начал применять что-либо кроме
   * перемещения
   * @return
   */
  boolean isRollbackAvailable();

  /**
   * Получить расстояние в "пикелях" до указанной координаты. Рассчитывается на основании текущих ПРИВЕДЕННЫХ
   * координат юнита. (getTranslatedToGameCoords)
   * @param to
   * @return
   */
  int calcDistanceTo(Coords to);

  /**
   * Получить расстояние в "пикелях" до указанной координаты. Рассчитывается на основании текущих РЕАЛЬНЫХ
   * координат юнита. (getCoords)
   * @param to
   * @return
   */
  int calcDistanceToTarget(Coords to);

  /**
   * Кол-во потраенных на перемещение единиц действия. Имеет смысл только до тех пор, пока можно выполнить откат
   * перемещения.
   * @return
   */
  int getTreatedActionPointsForMove();

  /**
   * Задать имя воина
   * @param title
   * @return
   */
  Warrior setTitle(String title);

  /**
   * Получить список способностей, которыми данный класс обладать не может
   * @return название/класс
   */
  Map<String, Class<? extends Ability>> getUnsupportedAbilities();

  /**
   * Перекрыть метод возврата владельца.
   * TODO поправить, чтобы работало БЕЗ этого метода
   * @return
   */
  Player getOwner();

  /**
   * дать воину артефакт. Если такой артефакт такого типа уже есть, то будет отказ
   * @param artifactClass класс даваемого артефакта
   * @return
   */
  Result<Artifact<Warrior>> giveArtifactToWarrior(Class<? extends Artifact<Warrior>> artifactClass);

  /**
   * добавить уже существующий артифакт. Если такой артифакт уже есть, то добавить второй нельзя
   * @param artifact
   * @return
   */
  Result<Artifact<Warrior>> attachArtifact(Artifact<Warrior> artifact);

  /**
   * Возвращает список артефактов воина
   * @return
   */
  Result<List<Artifact<Warrior>>> getArtifacts();

  void setTreatedActionPointsForMove(int treatedActionPointsForMove);


}