package com.example.clicka.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.TypeConverter
import com.example.clicka.interfaces.EntityWithId
import kotlinx.serialization.Serializable


@Entity(
    tableName = "action_table",
    indices = [Index("action_id")],
    foreignKeys = [
        ForeignKey(
            entity = ScenarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["scenario_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ]
)

@Serializable
data class ActionEntity(
    @PrimaryKey(autoGenerate = true) override var id:Long,
    @ColumnInfo(name = "scenario_id") var scenarioId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: ActionType,
    @ColumnInfo(name = "priority") val priority: Int,

//    Only for repeatable actions
    @ColumnInfo(name = "repeat_count") val repeatCount:Int?= null,
    @ColumnInfo(name = "is_repeat_infinite") val isRepeatInfinite:Boolean?= null,
    @ColumnInfo(name = "repeat_delay") val repeatDelay:Long?= null,

//    ActionType.CLICK
    @ColumnInfo(name = "press_duration") val pressDuration:Long?=null,
    @ColumnInfo(name = "x") val x: Int? = null,
    @ColumnInfo(name = "y") val y: Int? = null,

//    ActionType.SWIPE
    @ColumnInfo(name = "swipe_duration") val swipeDuration:Long?=null,
    @ColumnInfo(name = "fromX") val fromX: Int? = null,
    @ColumnInfo(name = "fromY") val fromY: Int?=null,
    @ColumnInfo(name= "toX") val toX: Int?=null,
    @ColumnInfo(name = "toY") val toY: Int?=null,

//    ActionType.PAUSE
    @ColumnInfo(name = "pause_duration") val pauseDuration:Long?=null,

): EntityWithId

enum class ActionType{
    CLICK,
    SWIPE,
    PAUSE,

}


internal class ActionTypeStringConverter{
    @TypeConverter
    fun fromString(value: String): ActionType=ActionType.valueOf(value)
    @TypeConverter
    fun toString(action:ActionType):String = action.toString()
}