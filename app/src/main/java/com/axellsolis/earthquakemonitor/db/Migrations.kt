package com.axellsolis.earthquakemonitor.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE earthquake ADD COLUMN is_saved INTEGER NOT NULL DEFAULT 1")
        }
    }
}