package api.entity.warrior;

import api.enums.ArmorClassEnum;

/**
 * Базовые атрибуты воина
 */
public interface WarriorSBaseAttributes {
  /**
   * ВОзвращает очки здоровья
   *
   * @return
   */
  int getHealth();

  /**
   * Изменяет значение здоровья
   *
   * @param deltaHealth
   * @return
   */
  int addHealth(int deltaHealth);

  /**
   * задать максимальное кол-во очков здоровья
   * @param maxHealth
   * @return
   */
  public WarriorSBaseAttributes setMaxHealth(int maxHealth);

  /**
   * возвращает очки магии
   * @return
   */
  int getManna();

  /**
   * Изменить на N очков магии
   *
   * @param deltaManna
   * @return
   */
  int addManna(int deltaManna);

  /**
   * Задать максимальное кол-во очков магии
   * @param maxManna
   * @return
   */
  WarriorSBaseAttributes setMaxManna(int maxManna);

  /**
   * класс брони
   */
  ArmorClassEnum getArmorClass();


  /**
   * задать класс брони
   * @param armorClass
   * @return
   */
  WarriorSBaseAttributes setArmorClass(ArmorClassEnum armorClass);

  /**
   * Дельта к цене передвижения, определяемому по классу брони
   */
  Integer getDeltaCostMove();


  /**
   * Задать коррекцию к цене за перемещение. Прибавляется к цене, рассчитанной исходя из класса брони воина
   * @param deltaCostMove
   * @return
   */
  WarriorSBaseAttributes setDeltaCostMove(int deltaCostMove);

  /**
   * Кол-во очков применения способностей за ход
   */
  int getAbilityActionPoints();

  /**
   * кол-во очков применения способностей. На ход
   * @param abilityActionPoints
   * @return
   */
  WarriorSBaseAttributes setAbilityActionPoints(int abilityActionPoints);

  /**
   * Кол-во очков применения способностей за ход
   */
  int getMaxAbilityActionPoints();

  /**
   * Максимальное кол-во очков применения способностей. На ход
   * @param maxAbilityActionPoints
   * @return
   */
  WarriorSBaseAttributes setMaxAbilityActionPoints(int maxAbilityActionPoints);

  /**
   * Очки обычных действий на ход
   * @return
   */
  int getActionPoints();

  /**
   * Задать очки обычных действий на ход
   * @param actionPoints
   * @return
   */
  WarriorSBaseAttributes setActionPoints(int actionPoints);

  /**
   * Изменить кол-во очков действия
   * @param deltaActionPoints
   * @return
   */
  int addActionPoints(int deltaActionPoints);

  /**
   * Получить максимальное кол-во очков действия на ход
   * @return
   */
  int getMaxActionPoints();

  /**
   * Задать максимальное количество очков действия на ход
   * @param maxActionPoints
   * @return
   */
  WarriorSBaseAttributes setMaxActionPoints(int maxActionPoints);

  /**
   * Получить кол-во рук
   * @return
   */
  int getHandsCount();

  /**
   * Указывает можно ли призывать этого воина
   * @return
   */
  boolean isSummonable();

  /**
   * получить максимальное кол-во очков жизни
   * @return
   */
  int getMaxHealth();

  /**
   * получить максимальное кол-во очков магии
   * @return
   */
  int getMaxManna();

  /**
   * получить удачу при атаке ближнего боя
   * @return
   */
  int getLuckMeleeAtack();

  /**
   * получить удачу при атаке дальнего боя
   * @return
   */
  int getLuckRangeAtack();

  /**
   * получить удачу при защите
   * @return
   */
  int getLuckDefence();

  WarriorSBaseAttributes setHealth(int health);

  WarriorSBaseAttributes setManna(int manna);

  WarriorSBaseAttributes setDeltaCostMove(Integer deltaCostMove);

  WarriorSBaseAttributes setHandsCount(int handsCount);

  WarriorSBaseAttributes setLuckMeleeAtack(int luckMeleeAtack);

  WarriorSBaseAttributes setLuckRangeAtack(int luckRangeAtack);

  WarriorSBaseAttributes setLuckDefence(int luckDefence);


  }
