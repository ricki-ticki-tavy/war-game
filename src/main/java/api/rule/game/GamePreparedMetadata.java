package api.rule.game;

import api.rule.ability.Ability;
import api.rule.ability.Modifier;
import api.rule.creature.BaseCreatureClass;
import api.rule.weapon.Weapon;
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
  public Map<String, Weapon> weapons = new ConcurrentHashMap<>();

  /**
   * базовые типы созданий, их характеристики и способности
   */
  public Map<String, BaseCreatureClass> baseCreatureClasses = new ConcurrentHashMap<>();


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
    return Optional.ofNullable(StringUtils.isEmpty(modifier.ref)
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
    return Optional.ofNullable(StringUtils.isEmpty(ability.ref)
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
  public Weapon findWeapon(Weapon weapon) {
    return Optional.ofNullable(StringUtils.isEmpty(weapon.ref)
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
  public BaseCreatureClass findBaseCreatureClass(BaseCreatureClass baseCreatureClass) {
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
  public void addWeapon(Weapon weapon) {
    if (StringUtils.isEmpty(weapon.ref)) {
      // полноценная запись для обработки и добавления в словарь
      if (weapon.additionalModifiers != null) {
        List<Modifier> weaponModifiers = new LinkedList<>(weapon.additionalModifiers);
        weapon.additionalModifiers.clear();
        weaponModifiers.stream().forEach(modifier -> weapon.additionalModifiers.add(findModifier(modifier)));

        Optional.ofNullable(weapons.get(weapon.id))
                .ifPresent(dupAbility -> {
                  throw new RuntimeException("Weapon with id \"" + dupAbility.id + "\" already exists");
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
  public void addCreatureClass(BaseCreatureClass baseCreatureClass) {
    if (StringUtils.isEmpty(baseCreatureClass.ref)) {
      // полноценная запись
      Optional.ofNullable(baseCreatureClasses.get(baseCreatureClass.id))
              .ifPresent(dupAbility -> {
                throw new RuntimeException("Base creature class with id \"" + dupAbility.id + "\" already exists");
              });

      // подтянем способности
      Optional.ofNullable(baseCreatureClass.abilities)
              .ifPresent(abilities -> {
                List<Ability> creatureClassAbilities = new LinkedList<>(abilities);
                abilities.clear();
                creatureClassAbilities.stream().forEach(ability ->
                        abilities.add(findAbility(ability))
                );
              });
      // подтянем оружие
      Optional.ofNullable(baseCreatureClass.weapons)
              .ifPresent(weapons -> {
                List<Weapon> creatureClassWeapons = new LinkedList<>(weapons);
                weapons.clear();
                creatureClassWeapons.stream().forEach(weapon ->
                        weapons.add(findWeapon(weapon))
                );
              });
      baseCreatureClasses.put(baseCreatureClass.id, baseCreatureClass);
    } else {
      // ссылка
      throw new RuntimeException("Reference to base creature class not allowed here ( \"" + baseCreatureClass.ref + "\")");
    }
  }


}
