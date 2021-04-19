package com.courses.calculadoraimc

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import ir.androidexception.datatable.DataTable
import ir.androidexception.datatable.model.DataTableHeader
import ir.androidexception.datatable.model.DataTableRow
import java.text.NumberFormat
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var sbPeso: Slider
    lateinit var sbAltura: Slider
    lateinit var btnCalcular: Button
    lateinit var btnLimpar: Button
    lateinit var dataTable: DataTable
    lateinit var tvIMC : TextView
    var imc: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sbPeso = findViewById(R.id.sbPeso)
        sbAltura = findViewById(R.id.sbAltura)
        dataTable = findViewById(R.id.dataTable)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnLimpar = findViewById(R.id.btnLimpar)
        tvIMC = findViewById(R.id.tvIMC)

        sbPeso.setLabelFormatter {
            val format = NumberFormat.getIntegerInstance()
            format.maximumFractionDigits = 0
            "${format.format(it)} kg"
        }

        sbAltura.setLabelFormatter {
            val format = NumberFormat.getNumberInstance()
            format.maximumFractionDigits = 2
            "${format.format(it)} m"
        }

        btnCalcular.setOnClickListener {
            imc = sbPeso.value / (sbAltura.value * sbAltura.value)
            tvIMC.text = ("""Resultados
                        |Peso : """ + sbPeso.value.formatIMC() + """
                        |Altura : """ + sbAltura.value.formatIMC() + """ 
                        |Seu IMC é : """ + imc.formatIMC()).trimMargin()
        }

        btnLimpar.setOnClickListener {
            sbPeso.value = sbPeso.valueFrom
            sbAltura.value = sbAltura.valueFrom
            tvIMC.text = ""
        }

        initDataTable()
    }

    private fun initDataTable() {

        val cabecalhos = DataTableHeader.Builder()
            .item("IMC", 5)
            .item("Classificação", 5)
            .item("Grau de Obesidade", 5)
            .build()

        val linhas = arrayListOf<DataTableRow>()
        for (x in 0..4) {
            var linha: DataTableRow? = null
            when (x) {
                0 -> linha = DataTableRow.Builder().value("18,5").value("Magreza").value("O").build()
                1 -> linha = DataTableRow.Builder().value("18,6 a 24,9").value("Normal").value("O").build()
                2 -> linha = DataTableRow.Builder().value("25,9 a 29,9").value("Sobrepeso").value("I").build()
                3 -> linha = DataTableRow.Builder().value("30,0 a 39,9").value("Obesidade").value("II").build()
                4 -> linha = DataTableRow.Builder().value("40,0").value("Obesidade Grave").value("III").build()
            }
            linhas.add(linha!!)
        }
        dataTable.header = cabecalhos
        dataTable.rows = linhas
        dataTable.inflate(this)
    }

}
/**
 * Formata o resultado do IMC.
 * @author Miguel
 * @return String
 **/
private fun Float.formatIMC(): String {
    val currencyInstance = NumberFormat.getNumberInstance()
    currencyInstance.maximumFractionDigits = 2

    return currencyInstance.format(this.toDouble()).replace(".", ",")
}