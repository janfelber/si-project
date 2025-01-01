<template>

  <div class="card-class" style="background-color: white">
    <div class="grading-table" style="border: 1px solid #d8d8f0; border-radius: .5rem">
      <v-card-title class="table-head">
        <div class="title-with-count">
          <h5>Hodnotenie</h5>
        </div>
      </v-card-title>

      <table class="grading-table">
        <thead>
        <tr>
          <th
              v-for="(header, i) in headers"
              :key="`header-${i}`"
              class="header-item"
          >
            {{ header }}
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(row,index)  in tableRows" :key="index" class="table-rows">
          <td class="cell" style="width: 5.5%;">{{row.text}}</td>
          <td class="cell" style="width: 5.5%;">
            <v-select v-if="row.dropdown"
                :items="['A', 'B', 'C', 'D', 'E', 'Fx']"
                v-model="row.selected"
            ></v-select>
            <v-text-field
                v-if="row.input"
                v-model="row.selected">
            </v-text-field>
            <v-select v-if="row.checkbox"
                      :items="['Áno', 'Nie']"
                      v-model="row.selected"
            ></v-select>

          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <v-btn  @click="save()" >Uložiť hodnotenie</v-btn>
  </div>

</template>


<script>
import axios from "axios";

export default {
  name: "GradingView",
  data(){
    return {
      headers: ["Kritéria", "Hodnotenie"],
      dialog: false,
      tableRows: [
        {
          text: 'PC member',
          dropdown: false,
          input: true,
          checkbox: false,
          selected: null
        },
        {
          text: 'Reviewer',
          dropdown: false,
          input: true,
          checkbox: false,
          selected: null
        },
        {
          text: 'Time',
          dropdown: false,
          input: true,
          checkbox: false,
          selected: null
        },
        {
          text: 'Zorientovanie sa študenta v danej problematike predovšetkým analýzou domácej a zahraničnej literatúry',
          dropdown: true,
          input: false,
          checkbox: false,
          selected: null
        },
        {
          text: 'Zorientovanie sa študenta v danej problematike predovšetkým analýzou domácej a zahraničnej literatúry',
          dropdown: true,
          input: false,
          checkbox: false,
          selected: null
        },
        {
          text: 'Vhodnosť zvolených metód spracovania riešenej problematiky',
          dropdown: true,
          input: false,
          checkbox: false,
          selected: null
        },
        {
          text: 'Rozsah a úroveň dosiahnutých výsledkov',
          dropdown: true,
          input: false,
          checkbox: false,
          selected: null
        },
        {
          text: 'Analýza a interpretácia výsledkov a formulácia záverov práce',
          dropdown: true,
          input: false,
          checkbox: false,
          selected: null
        },
        {
          text: 'Prehľadnosť a logická štruktúra práce',
          dropdown: true,
          input: false,
          checkbox: false,
          selected: null
        },
        {
          text: 'Formálna, jazyková a štylistická úroveň práce',
          dropdown: true,
          input: false,
          checkbox: false,
          selected: null
        },
        {
          text: 'Práca zodpovedá šablóne určenej pre ŠVK',
          dropdown: false,
          input: false,
          checkbox: true,
          selected: null
        },
        {
          text: 'Chýba názov práce v slovenskom alebo anglickom jazyku',
          dropdown: false,
          input: false,
          checkbox: true,
          selected: null
        },
        {
          text: 'Chýba meno autora alebo školiteľa',
          dropdown: false,
          input: false,
          checkbox: true,
          selected: null
        },
        {
          text: 'Chýba abstrakt v slovenskom alebo anglickom jazyku',
          dropdown: false,
          input: false,
          checkbox: true,
          selected: null
        },
        {
          text: 'Abstrakt nespĺňa rozsah 100 - 150 slov',
          dropdown: false,
          input: false,
          checkbox: true,
          selected: null
        },
        {
          text: 'Chýbajú kľúčové slová v slovenskom alebo anglickom jazyku',
          dropdown: false,
          input: false,
          checkbox: true,
          selected: null
        },
        {
          text: 'Chýba "Úvod", "Výsledky a diskusia" alebo "Záver"',
          dropdown: false,
          input: false,
          checkbox: true,
          selected: null
        },
        {
          text: 'Nie sú uvedené zdroje a použitá literatúra',
          dropdown: false,
          input: false,
          checkbox: true,
          selected: null
        },
        {
          text: 'V texte chýbajú referencie na zoznam bibliografie',
          dropdown: false,
          input: false,
          checkbox: true,
          selected: null
        },
        {
          text: 'V texte chýbajú referencie na použité obrázky a/alebo tabuľky',
          dropdown: false,
          input: false,
          checkbox: true,
          selected: null
        },
        {
          text: 'Obrázkom a/alebo tabuľkám chýba popis',
          dropdown: false,
          input: false,
          checkbox: true,
          selected: null
        },
        {
          text: 'Prínos (silné stránky) práce',
          dropdown: false,
          input: true,
          checkbox: false,
          selected: null
        },
        {
          text: 'Nedostatky (slabé stránky) práce',
          dropdown: false,
          input: true,
          checkbox: false,
          selected: null
        },
      ]
    }
  },
  methods: {
    save(){
      for(let item of this.tableRows){
        console.log(item.text + " " + item.selected);
      }
    }
  },
  mounted() {

  },
  computed:{

  }
}
</script>

<style scoped>


.grading-table .cell {
  border-left: 1px solid #d8d8f0;
  border-right: 1px solid #d8d8f0;
  padding: .5em;
  background: white;
}

.grading-table .cell:first-child {
  border-left: none;
}

.card-class {
  padding: 2.5rem 2.5rem .75rem;
}


.table-head {
  padding: .5em 1.5em;
  align-items: center;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid rgb(216, 216, 240);
}

.title-with-count {
  display: flex;
  align-items: center;
}

h5 {
  font-size: 1.125rem;
}

.router img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.buttons img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

table {
  border-collapse: collapse;
  width: 100%;
  table-layout: auto !important;
  word-wrap: break-word;
  border-radius: 0 0 0.5em 0.5em;
  overflow: hidden;
}

td {
  padding: 24px;
  text-align: center;
  border-bottom: 1px solid rgb(224, 242, 237);
}

.grading-table .header-item {
  padding: 30px 20px;
  font-size: 12px;
  text-transform: uppercase;
  border-left: 1px solid #d8d8f0;
  border-right: 1px solid #d8d8f0;
  background: #f7f7fc;
  border-bottom: 1px solid #d8d8f0;
}

.grading-table .header-item:first-child {
  border-left: none;
}

.grading-table .header-item:last-child {
  border-right: none;
}

.table-rows:nth-child(odd) {
  background-color: rgb(250, 250, 250);
}

.table-rows:nth-child(n):hover {
  background-color: rgb(244, 246, 245);
}
</style>