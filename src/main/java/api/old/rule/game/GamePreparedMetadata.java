package api.old.rule.game;

import api.old.rule.ability.Ability;
import api.old.rule.ability.Modifier;
import api.old.rule.creature.BaseWarriorClass;
import api.old.rule.weapon.XmlWeapon;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GamePreparedMetadata {
  /**
   * длина единичного отрезка длины в точках карты.
   */
  public int lengthOfSimpleLengthUnit;

  /**
   * Все возможные модификаторы
   */
  public Map<String, Modifier> modifiers = new ConcurrentHashMap<>();

  /**
   * Способности. разные
   */
  public Map<String, Ability> abilities = new ConcurrentHashMap<>();

  /**
   * Виды оружия
   */
  public Map<String, XmlWeapon> weapons = new ConcurrentHashMap<>();

  /**
   * базовые типы созданий, их характеристики и способности
   */
  public Map<String, BaseWarriorClass> baseCreatureClasses = new ConcurrentHashMap<>();


  /**
   * параметры и способности игрока
   */
//  public BasePlayer basePlayer;


  /**
   * Возвращает из справочника модификатор, соответствующий переданному
   *
   * @param modifier
   * @return
   */
  public Modifier findModifier(Modifier modifier) {
    return (StringUtils.isEmpty(modifier.ref) && StringUtils.isEmpty(modifier.id)) ? modifier
            : Optional.ofNullable(StringUtils.isEmpty(modifier.ref)
            ? modifiers.get(modifier.id)
            : modifiers.get(modifier.ref))
            .orElseThrow(() -> new RuntimeException("Unknown modifier with id \"" + modifier.ref + "\""));
  }

  /**
   * Поиск способности по справочнику на основе id или ref
   *
   * @param ability
   * @return
   */
  public Ability findAbility(Ability ability) {
    return (StringUtils.isEmpty(ability.ref) && StringUtils.isEmpty(ability.id)) ? ability
            : Optional.ofNullable(StringUtils.isEmpty(ability.ref)
            ? abilities.get(ability.id)
            : abilities.get(ability.ref))
            .orElseThrow(() ->
                    new RuntimeException("Unknown ability with id \"" + ability.ref + "\"")
            );
  }

  /**
   * Поиск оружия по справочнику на основе id или ref
   *
   * @param weapon
   * @return
   */
  public XmlWeapon findWeapon(XmlWeapon weapon) {
    return (StringUtils.isEmpty(weapon.ref) && StringUtils.isEmpty(weapon.id)) ? weapon
            : Optional.ofNullable(StringUtils.isEmpty(weapon.ref)
            ? weapons.get(weapon.id)
            : weapons.get(weapon.ref))
            .orElseThrow(() ->
                    new RuntimeException("Unknown weapon with id \"" + weapon.ref + "\"")
            );
  }

  /**
   * Поиск класса создания  по справочнику на основе id или ref
   *
   * @param baseCreatureClass
   * @return
   */
  public BaseWarriorClass findBaseCreatureClass(BaseWarriorClass baseCreatureClass) {
    return Optional.ofNullable(StringUtils.isEmpty(baseCreatureClass.ref)
            ? baseCreatureClasses.get(baseCreatureClass.id)
            : baseCreatureClasses.get(baseCreatureClass.ref))
            .orElseThrow(() ->
                    new RuntimeException("Unknown base creature class with id \"" + baseCreatureClass.ref + "\"")
            );
  }

  /**
   * Добавить в справочник модификатор атрибута
   *
   * @param modifier
   */
  public void addModifier(Modifier modifier) {
    if (StringUtils.isEmpty(modifier.ref)) {
      // полноценная запись
      if (StringUtils.isEmpty(modifier.id)) {
        return;
      }
      Optional.ofNullable(modifiers.get(modifier.id))
              .ifPresent(dupModifier -> {
                throw new RuntimeException("Modifier with id \"" + dupModifier.id + "\" already exists");
              });
      modifiers.put(modifier.id, modifier);
    } else {
      // ссылка
      throw new RuntimeException("Reference to  modifier not allowed here ( \"" + modifier.ref + "\")");
    }
  }


  /**
   * Добавление в справочник спосрбности
   *
   * @param ability
   * @return полноценная запись из словаря или полготовленный оригинал
   */
  public void addAbility(Ability ability) {
    if (StringUtils.isEmpty(ability.ref)) {
      // новая
      if (StringUtils.isEmpty(ability.id)) {
        return;
      }
      Optional.ofNullable(abilities.get(ability.id))
              .ifPresent(dupAbility -> {
                throw new RuntimeException("Ability with id \"" + dupAbility.id + "\" already exists");
              });

      List<Modifier> abilityModifiers = new LinkedList<>(ability.abilityModifiers);
      ability.abilityModifiers.clear();

      abilityModifiers.stream().forEach(modifier -> ability.abilityModifiers.add(findModifier(modifier)));

      abilities.put(ability.id, ability);
    } else {
      // ссылка
      throw new RuntimeException("Reference to  ability not allowed here ( \"" + ability.ref + "\")");
    }
  }


  /**
   * Добавление в справочник оружия
   *
   * @param weapon
   */
  public void addWeapon(XmlWeapon weapon) {
    if (StringUtils.isEmpty(weapon.ref)) {
      // полноценная запись для обработки и добавления в словарь
      if (StringUtils.isEmpty(weapon.id)) {
        return;
      }
      if (weapon.additionalModifiers != null) {
        List<Modifier> weaponModifiers = new LinkedList<>(weapon.additionalModifiers);
        weapon.additionalModifiers.clear();
        weaponModifiers.stream().forEach(modifier -> weapon.additionalModifiers.add(findModifier(modifier)));

        Optional.ofNullable(weapons.get(weapon.id))
                .ifPresent(dupAbility -> {
                  throw new RuntimeException("XmlWeapon with id \"" + dupAbility.id + "\" already exists");
                });

      }
      weapons.put(weapon.id, weapon);
    } else {
      // ссылка
      throw new RuntimeException("Reference to weapon not allowed here ( \"" + weapon.ref + "\")");
    }
  }


  /**
   * Добавить в справочник класс создания
   *
   * @param baseCreatureClass
   */
  public void addCreatureClass(BaseWarriorClass baseCreatureClass) {
    if (StringUtils.isEmpty(baseCreatureClass.ref)) {
      // полноценная запись
      Optional.ofNullable(baseCreatureClasses.get(baseCreatureClass.id))
              .ifPresent(dupAbility -> {
                throw new RuntimeException("Base creature class with id \"" + dupAbility.id + "\" already exists");
              });

      // подтянем способности
      Optional.ofNullable(baseCreatureClass.abilities)
              .ifPresent(creatureAbilities -> {
                creatureAbilities.stream().forEach(creatureAbility -> {
                          Optional.ofNullable(creatureAbility.ability).orElseThrow(() -> new RuntimeException("Ability in creaureAbility can't be null in class \"" + baseCreatureClass.id + "\""));
                          creatureAbility.ability = findAbility(creatureAbility.ability);
                        }
                );
              });

//
//      {
//        List<XmlWeapon> creatureClassWeapons = new LinkedList<>(weapons);
//        weapons.clear();
//        creatureClassWeapons.stream().forEach(weapon ->
//                weapons.add(findWeapon(weapon))
//        );
//      }
//

      // подтянем оружие
      Optional.ofNullable(baseCreatureClass.hands)
              .ifPresent(hands -> hands.stream().forEach(hand ->
                      Optional.ofNullable(hand.weapons)
                              .ifPresent(weapons -> {
                                        List<XmlWeapon> creatureClassWeapons = new LinkedList<>(weapons);
                                        weapons.clear();
                                        creatureClassWeapons.stream().forEach(weapon ->
                                                weapons.add(findWeapon(weapon))
                                        );
                                      }
                              )));
      baseCreatureClasses.put(baseCreatureClass.id, baseCreatureClass);
    } else {
      // ссылка
      throw new RuntimeException("Reference to base creature class not allowed here ( \"" + baseCreatureClass.ref + "\")");
    }
  }


}
