package api.game.map;

import api.core.Context;
import api.core.Owner;
import api.core.Result;
import api.game.ability.Modifier;
import api.entity.base.BaseEntityHeader;
import api.game.Influencer;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.enums.LifeTimeUnit;
import api.geo.Coords;
import api.geo.Rectangle;
import api.game.action.AttackResult;

import java.util.List;
import java.util.Map;

/**
 * Игрок
 */
public interface Player extends Owner {

  /**
   * Добавить в коллекцию воина
   *
   * @param coords
   * @param warriorBaseClassName
   * @return
   */
  Result<Warrior> createWarrior(String warriorBaseClassName, Coords coords);

  /**
   * Вернуть всех воинов
   *
   * @return
   */
  List<Warrior> getWarriors();

  /**
   * Вернуть всех воинов.Не копирует список в новый лист. Это оригинальный MAP с юнитами.
   * Только для внутреннего использования
   *
   * @return
   */
  @Deprecated
  Map<String, Warrior> getOriginWarriors();

  /**
   * Задать зону выставления воинов
   *
   * @param startZone
   */
  void setStartZone(Rectangle startZone);


  /**
   * Получить зону выставления фигурок
   *
   * @return
   */
  Rectangle getStartZone();

  /**
   * Задать игроку новый контекст. При этом из старого контекста должен быть выход
   *
   * @param newContext
   * @return
   */
  Result replaceContext(Context newContext);

  /**
   * Задать игроку новый контекст без проверок и переподключений
   *
   * @param newContext
   * @return
   */
  Result<Player> replaceContextSilent(Context newContext);

  /**
   * Получить контекст игрока
   *
   * @return
   */
  Result<Context> findContext();

  /**
   * Найти воина по его коду
   *
   * @param warriorId
   * @return
   */
  Result<Warrior> findWarriorById(String warriorId);

  /**
   * Установить статус готовность к игре
   *
   * @param ready
   * @return
   */
  Result<Player> setReadyToPlay(boolean ready);

  /**
   * Получить статус готовности к игре
   *
   * @return
   */
  boolean isReadyToPlay();

  /**
   * Переместить юнит на новые координаты
   *
   * @param warrior
   * @param to
   * @return
   */
  Result<Warrior> moveWarriorTo(Warrior warrior, Coords to);


  /**
   * Откатить перемещение воина
   * @return
   */
  Result<Warrior> rollbackMove(String warriorId);

  /**
   * Проверяет возможно ли движение данного югита в этом ходе
   * @param warrior
   * @return
   */
  Result<Warrior> ifWarriorCanMoveAtThisTurn(Warrior warrior);

  /**
   * Проверяет возможно ли выполнение атаки или применения способности данным юнитом в этом ходе
   * @param warrior
   * @return
   */
  Result<Warrior> ifWarriorCanActsAtThisTurn(Warrior warrior);

  /**
   * очищает воинов, артефакты и прочее у игрока
   *
   * @return
   */
  Result<Player> clear();

  /**
   * Удалить юнит игроком
   *
   * @param warriorId
   * @return
   */
  Result<Warrior> removeWarrior(String warriorId);

  /**
   * Вооружить воина предметом
   * @param warriorId
   * @param weaponClass
   * @return
   */
  Result<Weapon> giveWeaponToWarrior(String warriorId, Class<? extends Weapon> weaponClass);

  /**
   * Подготовка воина перед ходом игрока. Восстановление различных параметров до нормальных значений
   *
   * @return
   */
  Result<Player> prepareToAttackPhase();

  /**
   * Подготовка параметров юнита к фазе защиты. То есть когда ход игрока-владельца юнита закончен и ход переходит
   * к следующему игроку
   *
   * @return
   */
  Result<Player> prepareToDefensePhase();

  /**
   * Атаковать выбранным оружием другого воина
   * @param attackerWarriorId
   * @param targetWarriorId
   * @param weaponId
   * @return
   */
  Result<AttackResult> attackWarrior(String attackerWarriorId, String targetWarriorId, String weaponId);

  /**
   * Метод вызывается после того, как атакующим воином выполнены все расчеты оказываемых на атакуемого воина воздействий
   * тут плеер может добавить что-то еще от себя.
   * @return
   */
  Result<AttackResult> innerAttachToAttackToWarrior(AttackResult attackResult);

  /**
   * Этот метод вызывается когда воин игрока атакуется. В этом методе происходит анализ всех нанесенныхз уронов,
   * перерасчет их (в случае наличия сопротивления, брони и прочее) и применение урона и влияний к воину
   * @param attackResult
   * @return
   */
  Result<AttackResult> defenceWarrior(AttackResult attackResult);

  /**
   * добавить влияние юниту
   *
   * @param modifier
   * @param lifeTimeUnit
   * @param lifeTime
   * @return
   */
  Result<Influencer> addInfluenceToWarrior(String warriorId, Modifier modifier, Owner source, LifeTimeUnit lifeTimeUnit, int lifeTime);

  /**
   * получитьсписок воинов, участвовавших в этом ходе / защите
   * @return
   */
  Result<List<Warrior>> getWarriorsTouchedAtThisTurn();

  /**
   * Найти оружие по его id
   * @param warriorId
   * @param weaponId
   * @return
   */
  Result<Weapon> findWeaponById(String warriorId, String weaponId);


}
