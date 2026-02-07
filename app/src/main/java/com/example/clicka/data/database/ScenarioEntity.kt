package com.example.clicka.data.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.clicka.base
import kotlinx.serialization.Serializable


@Entity(tableName = "scenario_table")
@Serializable
data class ScenarioEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "repeat_count") val repeatCount:Int,
    @ColumnInfo(name = "is_repeat_infinite") val isRepeatInfinite:Boolean,
    @ColumnInfo(name = "max_duration") val maxDurationMin: Long,
    @ColumnInfo(name = "is_duration_infinite") val isDurationInfinite: Boolean,
    @ColumnInfo(name = "randomize") val randomize: Boolean,
)


@Serializable
data class ScenarioWithActions(
    @Embedded val scenario: ScenarioEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "scenario_id"
    )
    val Actions: List<ActionEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "scenario_id"
    )
    val stats: ScenarioStatsEntity?,
)