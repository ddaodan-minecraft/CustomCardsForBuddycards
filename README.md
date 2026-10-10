# Custom Cards for Buddycards (CCB)

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.20.1-brightgreen.svg" alt="Minecraft 1.20.1">
  <img src="https://img.shields.io/badge/Mod%20Loader-Forge%2047.2.0+-orange.svg" alt="Forge">
  <img src="https://img.shields.io/badge/KubeJS-6%20Supported-purple.svg" alt="KubeJS 6">
  <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="MIT License">
</p>

> **Custom Cards for Buddycards (CCB)** 是一个专为 **Buddycards (巴迪卡牌)** 设计的轻量级、数据驱动的扩展 Mod。  
> 无论是普通玩家还是整合包开发者，都可以通过 **极简 JSON 配置文件** 或 **KubeJS 6 脚本**，轻松创建专属卡组、卡包、卡册与奖章！

---

## 🌟 核心特性 (Features)

- 🗂️ **零代码 JSON 配置驱动**：只需在 `.minecraft/config/ccb/card_sets/` 下放置 JSON 文件即可新增卡组，首次启动自动生成详细模板。
- 📜 **KubeJS 6 深度集成**：通过 `CCBEvents.registerCardSets` 启动事件与流式 Builder 链式 API 动态注册卡牌。
- 🛡️ **软依赖与安全隔离**：未安装 KubeJS 时平滑降级，仅依赖 JSON 配置运行，绝不报错或崩溃；支持 `required_mod` 条件加载。
- 🎨 **原版像素级对标**：
  - 自动挂载模型与材质模板，**彻底告别紫黑方块**。
  - 严格还原原版卡牌三行纯净排版（名称、描述、卡组-单号），无多余英文杂项。
  - 完美支持闪卡（Foil ✦）效果与 Grade 1~5 评级系统。
- 🌈 **高级文本支持**：
  - **多行换行**：支持 `\n` 转义符或直接使用 JSON 字符串数组。
  - **颜色代码**：原生支持 `&` 和 `§` 颜色与样式代码（如 `&e金色`、`&c亮红`、`&l粗体`）。

---

## 🚀 快速上手 (Quick Start)

### 方式一：使用 JSON 配置文件（最推荐）

在 `.minecraft/config/ccb/card_sets/` 目录下创建任意 `.json` 文件（例如 `my_set.json`）：

```json
{
  "id": "elemental_beasts",
  "display_name": "元素异兽集",
  "card_count": 3,
  "required_mod": "",

  "pack": {
    "cards_per_pack": 3,
    "guaranteed_uncommon": true,
    "foil_chance": 0.08
  },

  "binder": {
    "color": "#2E8B57",
    "pages": 1
  },

  "medal": {
    "effect": "minecraft:fire_resistance",
    "effect_level": 1,
    "duration": 12000
  },

  "cards": [
    {
      "number": 1,
      "name": "烈焰幼犬",
      "rarity": "common",
      "description": "&c浑身燃烧着小火苗的小狗，\n&7常常在下界熔岩湖边玩耍。"
    },
    {
      "number": 2,
      "name": "冰霜雪貂",
      "rarity": "uncommon",
      "description": [
        "&b所过之处皆会留下薄冰，",
        "&f极其警惕且动作敏捷的小生灵。"
      ]
    },
    {
      "number": 3,
      "name": "&6大地岩龟",
      "rarity": "epic",
      "description": "&6背负着整座古老矿山的远古神龟。"
    }
  ]
}
```

#### JSON 配置字段说明

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| `id` | String | **必填**，卡组唯一英文标识（如 `elemental_beasts`） |
| `display_name` | String | 卡组游戏内显示名称（如 `元素异兽集`） |
| `card_count` | Integer | 卡组包含的总卡牌数量 |
| `required_mod` | String | *选填*，前置 Mod ID（未安装时自动跳过该卡组） |
| `pack.cards_per_pack` | Integer | 打开 1 个卡包获得的卡牌数量（默认 5） |
| `pack.guaranteed_uncommon` | Boolean | 是否开启保底机制（最后一张必定是非普通或更高） |
| `pack.foil_chance` | Double | 抽中闪卡（Foil ✦）的概率（如 0.08 为 8%） |
| `binder.color` | Hex String | 卡册代表色十六进制代码（如 `"#2E8B57"`） |
| `binder.pages` | Integer | 卡册页数（默认 1） |
| `medal.effect` | String | 奖章右键/激活时的药水效果 ID（如 `minecraft:speed`） |
| `medal.effect_level` | Integer | 药水效果等级（1 代表 I 级） |
| `medal.duration` | Integer | 药水持续时间（单位：Tick，20 Tick = 1 秒） |
| `cards` | Array | 卡牌列表：包含 `number`, `name`, `rarity` (common/uncommon/rare/epic), `description` |

---

### 方式二：使用 KubeJS 6 脚本

在 `kubejs/startup_scripts/` 目录下创建任意 `.js` 脚本（例如 `custom_cards.js`）：

```javascript
CCBEvents.registerCardSets(event => {
    event.createSet('ocean_creatures')
        .displayName('深海生物集')
        .cardCount(3)
        // 支持使用 \n 或字符串数组实现多行换行，原生支持 & 颜色代码
        .card(1, '海龟', 'common', '&a深海漫游者\n&7寿命极长的温和生物')
        .card(2, '海豚', 'uncommon', [
            '&b海洋中的领航者',
            '&7喜欢与船只同速竞渡'
        ])
        .card(3, '&6利维坦', 'epic', '&c终极深海霸主，传说中的巨兽')
        .pack(pack => {
            pack.cardsPerPack(3)
            pack.guaranteedUncommon(true)
            pack.foilChance(0.1) // 10% 闪卡几率
        })
        .binder(binder => {
            binder.color('#0066CC')
            binder.pages(1)
        })
        .medal(medal => {
            medal.effect('minecraft:dolphins_grace', 2)
            medal.duration(6000)
        });
});
```

---

## 🎨 自定义材质与贴图 (Custom Textures)

Mod 内置了原版 Buddycards 官方标准的材质与模型兜底。如果你想为卡组定制专属贴图，只需遵循命名规则放置贴图：

### 物品 Registry ID 规范
- **卡包**：`ccb:pack_<卡组ID>`
- **卡册**：`ccb:binder_<卡组ID>`
- **奖章**：`ccb:medal_<卡组ID>`
- **卡牌**：`ccb:card_<卡组ID>_<编号>`

### 贴图覆盖路径
将对应的同名 `.png` 图片放入以下路径之一：
- 资源包路径：`assets/ccb/textures/item/`
- KubeJS 路径：`kubejs/assets/ccb/textures/item/`

例如：`kubejs/assets/ccb/textures/item/card_elemental_beasts_1.png`

---

## 🛠️ 本地构建 (Building)

本项目采用现代化的 `NeoForged ModDevGradle (LegacyForge)` 进行构建：

### 环境需求
- JDK 17 或更高版本

### 构建命令

```bash
# 克隆仓库
git clone https://github.com/ddaodan-minecraft/CustomCardsForBuddycards.git
cd CustomCardsForBuddycards

# 编译并生成 Jar (Windows)
.\gradlew.bat build

# 编译并生成 Jar (Linux / macOS)
./gradlew build
```

构建生成的 Mod Jar 文件位于：
`build/libs/CCB-1.20.1-1.0.0.jar`

---

## 📄 开源许可 (License)

本项目基于 **[MIT License](LICENSE)** 协议开源。你可以自由地在整合包中集成、分发或对其进行二次修改。
