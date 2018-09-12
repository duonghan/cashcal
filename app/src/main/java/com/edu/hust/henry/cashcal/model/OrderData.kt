package com.edu.hust.henry.cashcal.model

/**
 * Project: cashcal
 * Created by DuongHV.
 * Copyright (c) 2018 - HUST.
 */

data class OrderData(var dayofweek: Int = 1, var price: Int = 0, var costs: MutableList<Int> = mutableListOf(), var notes: String = "", var uuid: String = "")