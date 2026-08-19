package com.moddedmite.mitemod.goki_stats.api.stat;

import com.moddedmite.mitemod.goki_stats.common.stat.StatMaxHealth;
import com.moddedmite.mitemod.goki_stats.common.stat.StatReaper;
import com.moddedmite.mitemod.goki_stats.common.stat.damage.*;
import com.moddedmite.mitemod.goki_stats.common.stat.movement.StatClimbing;
import com.moddedmite.mitemod.goki_stats.common.stat.movement.StatSteadyGuard;
import com.moddedmite.mitemod.goki_stats.common.stat.special.leaper.StatLeaperH;
import com.moddedmite.mitemod.goki_stats.common.stat.special.leaper.StatLeaperV;
import com.moddedmite.mitemod.goki_stats.common.stat.special.leaper.StatStealth;
import com.moddedmite.mitemod.goki_stats.common.stat.tool.ToolSpecificStat;
import com.moddedmite.mitemod.goki_stats.common.stat.tool.StatBowmanship;
import com.moddedmite.mitemod.goki_stats.common.stat.tool.StatChopping;
import com.moddedmite.mitemod.goki_stats.common.stat.tool.StatDigging;
import com.moddedmite.mitemod.goki_stats.common.stat.tool.StatMining;
import com.moddedmite.mitemod.goki_stats.common.stat.tool.StatSwordsmanship;

public interface Stats {
    ToolSpecificStat MINING = new StatMining(0, "grpg_Mining", 10);
    ToolSpecificStat DIGGING = new StatDigging(1, "grpg_Digging", 10);
    ToolSpecificStat CHOPPING = new StatChopping(2, "grpg_Chopping", 10);
    DamageSourceProtectionStat PROTECTION = new StatProtection(4, "grpg_Protection", 10);
    DamageSourceProtectionStat TEMPERING = new StatTempering(5, "grpg_Tempering", 10);
    DamageSourceProtectionStat TOUGH_SKIN = new StatToughSkin(6, "grpg_ToughSkin", 10);
    DamageSourceProtectionStat STAT_FEATHER_FALL = new StatFeatherFall(7, "grpg_FeatherFall", 10);
    StatBase LEAPER_H = new StatLeaperH(8, "grpg_LeaperH", 10);
    StatBase LEAPER_V = new StatLeaperV(9, "grpg_LeaperV", 10);
    StatBase CLIMBING = new StatClimbing(11, "grpg_Climbing", 10);
    StatBase PUGILISM = new StatPugilism(12, "grpg_Pugilism", 10);
    ToolSpecificStat SWORDSMANSHIP = new StatSwordsmanship(13, "grpg_Swordsmanship", 10);
    ToolSpecificStat BOWMANSHIP = new StatBowmanship(14, "grpg_Bowmanship", 10);
    StatBase REAPER = new StatReaper(15, "grpg_Reaper", 10);
    StatBase STEALTH = new StatStealth(19, "grpg_Stealth", 10);
    StatBase STEADY_GUARD = new StatSteadyGuard(18, "grpg_Steady_Guard", 10);
    StatMaxHealth MAX_HEALTH = new StatMaxHealth(21, "grpg_Health", 10);
    StatBase ROLL = new StatRoll(22, "grpg_Roll", 10);
}