// 示例 KubeJS 启动脚本：kubejs/startup_scripts/custom_buddycards.js
// 使用 CCB (Custom Cards for Buddycards) 扩展！

CCBEvents.registerCardSets(event => {
    // 1. 创建一套包含 9 张卡牌的自定义卡组
    event.createSet('dungeon_monsters')
        .displayName('地牢怪物集')
        .cardCount(9)
        .card(1, '腐化僵尸', 'common', '游荡在古老地牢的亡者')
        .card(2, '骷髅射手', 'common', '百步穿杨的骨架')
        .card(3, '剧毒蜘蛛', 'common', '喷吐蛛丝的小型猎手')
        .card(4, '怨灵苦力怕', 'uncommon', '悄无声息地接近你')
        .card(5, '暗影潜行者', 'uncommon')
        .card(6, '下界骑士', 'uncommon')
        .card(7, '地牢领主', 'rare', '地牢深处的强大首领')
        .card(8, '深渊支配者', 'rare')
        .card(9, '虚空龙魂', 'epic', '传说中的终极生物卡牌')
        .pack(pack => {
            pack.cardsPerPack(4)           // 每个卡包开出 4 张卡
            pack.guaranteedUncommon(true)  // 开启保底（最后一张必出非普通以上）
            pack.foilChance(0.08)          // 8% 概率出现闪卡 (Foil ✦)
        })
        .binder(binder => {
            binder.color('#8B0000')        // 深红色卡册
        })
        .medal(medal => {
            medal.effect('minecraft:strength', 2) // 佩戴/激活给予力量 II 效果
            medal.duration(6000)
        });

    // 2. 创建一个只在安装了机械动力(Create)时启用的卡组
    event.createSet('create_engineering')
        .displayName('动力工程学')
        .requiredMod('create') // 软依赖：未安装 create 时自动跳过
        .cardCount(3)
        .card(1, '黄铜动力机', 'common')
        .card(2, '飞轮齿轮', 'uncommon')
        .card(3, '蒸汽引擎', 'rare');
});
