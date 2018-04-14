package com.kochmarevsevolod.lazycook;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;

import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper{
  public static final String COLUMN_ID = "_id";
  public static final String TABLE_DISH = "Dishes";
  public static final String COLUMN_NAME_DISH = "name_dish";
  public static final String COLUMN_RECIPE = "recipe";
  public static final String COLUMN_SALT = "salt";
  public static final String COLUMN_STOVE = "stove";
  public static final String COLUMN_FAVORITE = "favorite";

  // таблица Ingredients
  public static final String TABLE_INGR = "Ingredients";
  public static final String COLUMN_NAME_INGREDIENT = "name_ingredient";

  // таблица Dish-Ingredient
  public static final String TABLE_LINK = "DishIngredient";
  public static final String COLUMN_ID_DISH = "id_dish";
  public static final String COLUMN_ID_INGREDIENT = "id_ingredient";
  public static final String COLUMN_AMOUNT = "amount";
  public static final String COLUMN_UNIT = "unit";

  private static final String DATABASE_NAME = "lazycook.db";
  private static final int DATABASE_VERSION = 1;

  // Скрипты создания таблиц
  private static final String DATABASE_CREATE_DISH = "create table "
          + TABLE_DISH + "(" + COLUMN_ID
          + " integer primary key autoincrement, " + COLUMN_NAME_DISH
          + " text not null," + COLUMN_RECIPE
          + " text not null," + COLUMN_SALT
          + " integer not null," + COLUMN_STOVE
          + " integer not null," + COLUMN_FAVORITE
          + " integer not null);";

  private static final String DATABASE_CREATE_INGR = "create table "
          + TABLE_INGR + "(" + COLUMN_ID
          + " integer primary key autoincrement, " + COLUMN_NAME_INGREDIENT
          + " text not null);";

  private static final String DATABASE_CREATE_LINK = "create table "
          + TABLE_LINK + "(" + COLUMN_ID
          + " integer primary key autoincrement, " + COLUMN_ID_DISH
          + " integer not null REFERENCES " + TABLE_DISH + ", " + COLUMN_ID_INGREDIENT
          + " integer not null REFERENCES " + TABLE_INGR + ", " + COLUMN_AMOUNT
          + " integer not null," + COLUMN_UNIT
          + " text not null);";
  //скрипты заполнения таблиц (тестовый вариант, в последствии буду подключать готовую БД или/и обращаться к БД на сервере)
  /*private static final String DATABASE_FILL_DISH = "INSERT into "+TABLE_DISH+" Values (1, 'Плов', 'Варим полтора кило баранины, " +
  "и полтора кило риса. Жарим лук и морковь. Добавляем подсолнечного масла и держим в казане. Охапка дров и плов готов', 0, 1, 0)," +
  " (2, 'Бутерброд с хлебом', 'Берем батон. Отрезаем два куска хлеба. Мажем один сливочным маслом. " +
  "Кладем сверху второй. Бутерброд с хлебом готов', 0, 0, 1)," +
  " (3, 'Физраствор', 'Берем поллитра воды и чайную " +
  "ложку соли. Перемешиваем. Физраствор готов', 1, 0, 0)," +
  " (4, 'Салат «Элитный»', 'Берем две банки шпрот, "+
  "добавляем килограмм вареной картошки кубивами, четыре сырка «Дружба» и полкило свежих огурцов, заправляем огромным " +
  "количеством майонеза, после чего солим. Приятного аппетита', 1, 0, 0);";
  // - - - - */
  private static final String DATABASE_FILL_INGR = "INSERT Into " + TABLE_INGR + " VALUES(1, 'Картошка')," +
  " (2, 'Хлеб')," +
  " (3, 'Лук')," +
  " (4, 'Подсолнечное масло')," +
  " (5, 'Сливочное масло')," +
  " (6, 'Морковь')," +
  " (7, 'Баранина')," +
  " (8, 'Огурец')," +
  " (9, 'Шпроты')," +
  " (10, 'Рис')," +
  " (11, 'Вода')," +
  " (12, 'Майонез')," +
  " (13, 'Сырок «Дружба»');";/*
  // - - - -
  private static final String DATABASE_FILL_LINK = "INSERT Into " + TABLE_LINK + " VALUES(1, 1, 3, 500, 'грамм')," +
  " (2, 1, 4, 400, 'миллилитров')," +
  " (3, 1, 6, 1000, 'грамм')," +
  " (4, 1, 7, 1500, 'грамм')," +
  " (5, 1, 10, 1500, 'грамм')," +
  " (6, 2, 2, 1, 'батон')," +
  " (7, 3, 11, 500, 'миллилитров')," +
  " (8, 4, 1, 1000, 'грамм')," +   // три дублированных связи с разным названием меры
  " (9, 4, 1, 1, 'килограмм')," +
  " (10, 4, 1, 1, 'кило')," +
  " (11, 4, 9, 2, 'банки')," +
  " (12, 4, 8, 500, 'грамм')," +
  " (13, 4, 12, 1, 'пачка')," +
  " (14, 4, 13, 4, 'пачки');";*/
  
  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
      database.execSQL(DATABASE_CREATE_DISH);
      database.execSQL(DATABASE_CREATE_INGR);
      database.execSQL(DATABASE_CREATE_LINK);
	  //database.execSQL(DATABASE_FILL_DISH);
	  database.execSQL(DATABASE_FILL_INGR);
	  //database.execSQL(DATABASE_FILL_LINK);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISH);
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGR);
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINK);
      onCreate(db);
  }
}