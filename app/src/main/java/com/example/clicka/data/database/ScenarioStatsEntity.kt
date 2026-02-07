package com.example.clicka.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.clicka.interfaces.EntityWithId
import kotlinx.serialization.Serializable


@Entity(
    tableName = "scenario_stats_table",
    indices = [Index("scenario_id")],
    foreignKeys = [ForeignKey(
        entity = ScenarioEntity::class,
        parentColumns =["id"],
        childColumns = ["scenario_id"],
        onDelete = ForeignKey.CASCADE
    )]
)

@Serializable
data class ScenarioStatsEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long,
    @ColumnInfo(name= "scenario_id") val scenarioId: Long,
    @ColumnInfo(name="last_start_timestampMs") val lastStartTimestampMs: Long,
    @ColumnInfo(name = "start_count") val startCount: Long,
): EntityWithId