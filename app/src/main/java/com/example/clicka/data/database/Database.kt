package com.example.clicka.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.clicka.base.ScenarioStats

import javax.inject.Singleton

@Database(
    entities = [
        ScenarioEntity::class,
        ActionEntity::class,
        ScenarioStatsEntity::class,
    ],
    version = DATABASE_VERSION,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to=2)
    ]

)

@TypeConverters(
    ActionTypeStringConverter::class
)


@Singleton
abstract class Database: RoomDatabase(){
    abstract fun ScenarioDao(): ScenarioDao
}

const val DATABASE_VERSION = 2