package api.old.game;

import java.util.List;

/**
 * Игрок и все его атрибуты
 */
public class Player {
  /**
   * Имя игрока
   */
  String name;

  /**
   * Кол-во очков магии
   */
  int manna;

  /**
   * Создания игрока
   */
  List<Creature> creatures;

  /**
   * СОздания,которыми игрок пользовался в данном ходе
   */
  List<Creature> usedOnThisRound;


}
