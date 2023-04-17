package kr.easw.lesson5;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * 해당 문제는 간단한 RPG 게임의 예제입니다.
 * <p>
 * 해당 문제는 다음의 제한을 가지고 있습니다 :
 * - {@link PlayerActionController}을 상속받는 클래스를 최소 1개 사용해야 합니다.
 * - 메서드는 getActionController의 수정만이 허용됩니다.
 * <p>
 * 해당 문제는 다음과 같은 추가 조건을 가지고 있습니다.
 * - [필수] {@link PlayerActionController}을 구현해 코드가 작동해야 합니다.
 * - [필수] private로 지정된 메서드와 필드는 사용해서는 안됩니다.
 * - [선택] 가능한 최고점에 도달하세요.
 * - [선택] InventoryData의 메서드를 활용해 아이템을 사용하세요.
 * - [선택] 최대한 높은 체력을 남기세요.
 * - [선택] 최대한 많은 피해를 입히세요.
 * - [선택] 승리하세요.
 * <p>
 * 해당 문제는 {@link PlayerActionController}을 어떻게 구현하느냐에 따라 최대로 나올 수 있는 점수가 다르도록 설계되었습니다.
 * 너무 어려운 경우, 메서드 구현 없이 {@link PlayerActionController}을 구현하는것만으로도 구동됩니다.
 * <p>
 * 최고점을 받고자 하는 경우, AWARD 아이템 또한 사용해야 합니다.
 */
public class SimpleIdleRpgExample {
    private static final ItemData POTION = new ItemData("포션", 0, 0, data -> {
        data.hp += 25;
    });

    private static final ItemData LARGE_POTION = new ItemData("대형 포션", 0, 0, data -> {
        data.hp += 50;
    });

    private static final ItemData POISON = new ItemData("독극물", 0, 0, data -> {
        data.hp -= 10;
    });

    private static final ItemData SWORD = new ItemData("검", 5, 1, data -> {
        data.score += 5;
    });

    private static final ItemData SHIELD = new ItemData("방패", 1, 20, data -> {
        data.score += 5;
    });

    private static final ItemData REWARD = new ItemData("보상 아이템", 0, 0, data -> {
        data.score += 5;
    });


    private static final EnemyData[] enemies = new EnemyData[]{new EnemyData(15, 8, new ItemData[]{LARGE_POTION, POTION, SWORD, SWORD, SHIELD, SHIELD,}), new EnemyData(25, 17, new ItemData[]{POTION, POISON, SHIELD, SWORD, POTION, REWARD}), new EnemyData(50, 7, new ItemData[]{LARGE_POTION, POTION, REWARD, REWARD, SHIELD, SWORD, SHIELD, SWORD}), new EnemyData(100, 16, new ItemData[]{}),};


    public static void main(String[] args) throws InterruptedException {
        CharacterData characterData = new CharacterData();
        InventoryData inventoryData = new InventoryData();
        for (EnemyData enemyData : enemies) {
            while (enemyData.hp > 0) {
                characterData.isAwarding = false;
                try {
                    getActionController().onPlayerTurn(characterData, inventoryData, enemyData);
                    int damageDealt = (characterData.weapon == null ? 1 : characterData.weapon.power);
                    characterData.score += damageDealt;
                    enemyData.hp = Math.max(0, enemyData.hp - damageDealt);
                    System.out.println(damageDealt + "의 피해를 입힙니다. 적의 남은 체력 " + enemyData.hp);
                    if (characterData.weapon != null && characterData.weapon.durability <= 1) {
                        characterData.weapon = null;
                        System.out.println("무기가 부숴졌습니다.");
                    } else {
                        if (characterData.weapon != null) characterData.weapon.durability -= 1;
                    }
                } catch (IllegalStateException ex) {
                    System.out.println(ex.getMessage());
                }

                if (enemyData.hp != 0) {
                    int damage = Math.max(0, enemyData.power - (characterData.weapon == null ? 0 : characterData.weapon.defence));
                    characterData.score += damage;
                    characterData.hp = Math.max(0, characterData.hp - damage);
                    System.out.println(damage + "의 피해를 입었습니다. 남은 체력 " + characterData.hp);
                    if (characterData.hp <= 0) {
                        System.out.println("패배하였습니다.");
                        System.out.println("최종 점수: " + characterData.score);
                        return;
                    }
                } else {
                    System.out.println("적을 처치하였습니다.");
                    characterData.isAwarding = true;
                    getActionController().onAward(characterData, inventoryData, enemyData.getDrops());
                }
                Thread.sleep(650L);
            }
        }
        System.out.println("승리하였습니다!");
        System.out.println("최종 점수: " + (characterData.score + characterData.hp * 1.5));
    }

    private static PlayerActionController getActionController() {
        return new PlayerActionController();
    }

    // 해당 예제는 문제를 낸 출제자 기준으로 최선을 다한 코드입니다.
    // 해당 코드가 최고점이 아닐 수 있으며, 이보다 더 높은 점수가 나올 수 있습니다.
    // 해당 코드 상으로 최종 점수는 368점입니다.
    //
    // 해당 코드는 포션과 검, 방패의 인덱스를 자동으로 지정하며, 이를 교체하는 형식으로 메커니즘을 구성하였습니다.
    // 포션이 존재하는 경우 다음의 흐름을 따릅니다.
    //
    //                                          ┌──   예   ──> 방패가 존재하는가?  ────┬─── 예 ────> 방패를 착용한다
    // 무기가 없거나, 무기의 방어력이 1 이하인가?  ───┼                                 아니오
    //                                          └──  아니오 ──> 포션을 사용한다  <─────┘
    //
    // 장착된 무기가 없는 경우, 다음의 흐름을 따릅니다.
    //
    // 무기가 없는가? ── 예 ──> 검을 장착한다
    //
    // 장착된 무기가 존재하는 경우, 다음의 흐름을 따릅니다.
    //
    // 무기가 없거나 무기의 방어력이 1 이하인가? ─── 예 ───> 들어올 피해의 임계선보다 적의 공격력이 큰가? ─── 예 ───┐
    //                   방패를 장착한다 <─── 예 ───  캐릭터의 체력이 적의 피해량과 HP 버퍼를 더한것보다 적은가? ─┘
    private static class PlayerActionController {

        private static final int THRESHOLD_HP_BUFFER = 2;

        private static final int DAMAGE_THRESHOLD = 15;

        private static int potionIndex = -1;

        private static int swordIndex = -1;

        private static int shieldIndex = -1;


        public void onPlayerTurn(CharacterData characterData, InventoryData inventoryData, EnemyData enemyData) {
            if (potionIndex != -1) {
                if ((characterData.weapon == null || characterData.weapon.defence <= 1) && shieldIndex >= 7) {
                    inventoryData.equip(characterData, shieldIndex--);
                }
                inventoryData.consume(characterData, potionIndex--);
            }
            if (characterData.weapon == null && swordIndex >= 5) {
                inventoryData.equip(characterData, swordIndex--);
            }

            if ((characterData.weapon == null || characterData.weapon.defence <= 1) && (DAMAGE_THRESHOLD <= enemyData.power && (characterData.hp <= enemyData.power + THRESHOLD_HP_BUFFER) && shieldIndex >= 7)) {
                inventoryData.equip(characterData, shieldIndex--);
            }
        }


        public void onAward(CharacterData characterData, InventoryData data, ItemData[] itemData) {
            for (int i = 0; i < itemData.length; i++) {
                if (itemData[i].itemName.contains("포션")) {
                    data.acquire(characterData, itemData, i, ++potionIndex);
                } else if (itemData[i].itemName.equals("검")) {
                    if (swordIndex >= 7)
                        continue;
                    if (swordIndex == -1)
                        swordIndex = 4;
                    data.acquire(characterData, itemData, i, ++swordIndex);
                } else if (itemData[i].itemName.equals("방패")) {
                    if (shieldIndex >= 9)
                        continue;
                    if (shieldIndex == -1)
                        shieldIndex = 6;
                    data.acquire(characterData, itemData, i, ++shieldIndex);
                }
            }
        }
    }

    private static class CharacterData {
        private boolean isAwarding;

        private int hp = 50;

        private ItemData weapon = new ItemData("목검", 3, 5, null);

        private int score = 0;

        public int getHp() {
            return hp;
        }

        public ItemData getWeapon() {
            return weapon;
        }

        public int getScore() {
            return score;
        }
    }

    private static class ItemData implements Cloneable {
        private final String itemName;

        private final int power;

        private final int defence;

        private int durability = 5;

        private Consumer<CharacterData> action;

        public ItemData(String itemName, int power, int defence, Consumer<CharacterData> itemAction) {
            this.itemName = itemName;
            this.power = power;
            this.defence = defence;
            this.action = itemAction;
        }

        public int getDurability() {
            return durability;
        }

        public int getDefence() {
            return defence;
        }

        public int getPower() {
            return power;
        }

        public String getItemName() {
            return itemName;
        }

        public Consumer<CharacterData> getAction() {
            return action;
        }

        @Override
        protected ItemData clone() {
            ItemData data = new ItemData(itemName, power, defence, action);
            data.durability = durability;
            return data;
        }
    }

    private static class EnemyData {
        private int hp;

        private int power;

        private ItemData[] drops;

        public EnemyData(int hp, int power, ItemData[] drops) {
            this.hp = hp;
            this.power = power;
            this.drops = drops;
        }

        public ItemData[] getDrops() {
            return drops;
        }

        public int getPower() {
            return power;
        }

        public int getHp() {
            return hp;
        }

        private void setPower(int power) {
            this.power = power;
        }

        private void setHp(int hp) {
            this.hp = hp;
        }

        private void setDrops(ItemData[] drops) {
            this.drops = drops;
        }
    }

    private static class InventoryData {
        private final ItemData[] items = new ItemData[10];

        /**
         * 주어진 칸에 있는 장비를 장착합니다.
         * 해당 칸에 장비가 없으면 턴을 낭비합니다.
         */
        public void equip(CharacterData data, int slot) {
            if (items[slot] == null) {
                throw new IllegalStateException("해당 칸에 장비가 없습니다! 턴을 낭비합니다.");
            }
            ItemData equipped = data.weapon;
            data.weapon = items[slot].clone();
            items[slot] = equipped;
            if (!data.isAwarding) throw new IllegalStateException("장비 " + data.weapon.itemName + "을 장착합니다.");
        }

        /**
         * 주어진 칸에 있는 장비를 사용합니다.
         * 해당 칸에 장비가 없으면 턴을 낭비합니다.
         */
        public void consume(CharacterData data, int slot) {
            if (data.isAwarding) {
                System.out.println("보상 수령중에는 사용이 불가능합니다.");
                return;
            }
            if (items[slot] == null) {
                throw new IllegalStateException("해당 칸에 장비가 없습니다! 턴을 낭비합니다.");
            }

            if (items[slot].getAction() == null) {
                throw new IllegalStateException("소모 불가능한 장비입니다! 턴을 낭비합니다.");
            }
            ItemData item = items[slot];
            item.getAction().accept(data);
            items[slot] = null;
            throw new IllegalStateException("아이템 " + item.getItemName() + "을 사용합니다.");
        }

        /**
         * 보상 배열에서 아이템을 획득합니다.
         * 전투 도중에 사용시 턴을 낭비합니다.
         */
        public void acquire(CharacterData data, ItemData[] rewardArray, int slot, int targetSlot) {
            if (!data.isAwarding) {
                throw new IllegalStateException("보상 수령중이 아닐때 아이템 획득을 시도하였습니다. 턴이 낭비됩니다.");
            }
            if (items[targetSlot] != null) {
                System.out.println("아이템 " + items[targetSlot].getItemName() + "을 버렸습니다.");
            }
            ItemData itemTarget = rewardArray[slot];
            if (itemTarget == null) {
                return;
            }
            rewardArray[slot] = null;
            items[targetSlot] = itemTarget.clone();
            System.out.println("아이템 " + itemTarget.getItemName() + "을 얻었습니다.");
        }

        /**
         * 대상 위치에 있는 아이템을 반환합니다.
         */
        public ItemData getItemsAt(int index) {
            return items[index];
        }


        /**
         * 인벤토리의 최대 크기를 받습니다.
         */
        public int getMaxSize() {
            return items.length;
        }


        /**
         * 대상 위치에 있는 아이템을 삭제합니다.
         */
        public void remove(CharacterData data, int slot) {
            if (items[slot] != null) {
                ItemData item = items[slot];
                items[slot] = null;
                if (!data.isAwarding) throw new IllegalStateException("아이템 " + item.getItemName() + "을 버렸습니다.");
            }
        }
    }

}
