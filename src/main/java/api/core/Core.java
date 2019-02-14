package api.core;

import api.entity.stuff.Artifact;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.entity.weapon.Weapon;
import api.enums.EventType;
import api.game.map.Player;
import api.game.map.metadata.GameRules;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * взаимодействие с игрой.
 */
public interface Core {
  int getRandom(int min, int max);

  /**
   * Создать  игру
   *
   * @param userGameCreator
   * @param gameRules
   * @param map
   * @return
   */
  Result<Context> createGameContext(Player userGameCreator, GameRules gameRules, InputStream map, String gameName, boolean hidden);

  /**
   * Найти контекст по его UID
   * @param contextId
   * @return
   */
  Result<Context> findGameContextByUID(String contextId);

  /**
   * Удалить игру и ее контекст с сервера
   * @param contextId
   */
  Result<Context> removeGameContext(String contextId);

  /**
   * Отправить сообщение о событии
   * Событие сначала обрабатывается обработчиками привязанными к контексту, п потом без привязки к контексту,
   *
   * @param event
   * @return
   */
  Event fireEvent(Event event);

  /**
   * Подписаться на событие
   *
   * @param context    контекст, в рамках которого интересует данное событие. Если null, то по всем контекстам
   * @param consumer
   * @param eventTypes
   * @return
   */
  String subscribeEvent(Context context, Consumer<Event> consumer, EventType... eventTypes);

  /**
   * Отписаться от события
   *
   * @param context    контекст, в рамках которого интересует данное событие. Если null, то по всем контекстам
   * @param consumerId
   * @param eventTypes
   * @return
   */
  Result<Context> unsubscribeEvent(Context context, String consumerId, EventType... eventTypes);

  /**
   * подключить к игре игрока.
   *
   * @param playerName
   * @return
   */
  Result loginPlayer(String playerName);

  /**
   * Вернуть пользователя из списка вошедших в игру
   * @param playerName
   * @return
   */
  Result<Player> findUserByName(String playerName);

  /**
   * Получить список активных контекстов
   * @return
   */
  List<Context> getContextList();

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
   * Ищет базовый класс воина по его названию
   * @param className
   * @return
   */
  Result<Class<? extends WarriorBaseClass>> findWarriorBaseClassByName(String className);

  /**
   * Ищет базовый класс воина по его названию
   * @param weaponName
   * @return
   */
  Result<Class<? extends Weapon>> findWeaponByName(String weaponName);

  /**
   * Ищет базовый класс артефакта для воина по его названию
   * @param artifactName
   * @return
   */
  Result<Class<? extends Artifact<Warrior>>> findArtifactForWarrior(String artifactName);
}
