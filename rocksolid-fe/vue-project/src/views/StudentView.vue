
<template>

      <v-app>
        <h1>Nahravas pracu</h1>
      <v-form v-model="valid">
        <v-container>
          <v-row>
            <v-col md="4">
              <v-text-field
                  v-model="firstname"
                  label="Meno"
                  required
              ></v-text-field>

              <v-text-field
                  v-model="lastname"
                  label="Priezvisko"
                  required
              ></v-text-field>


                <v-text-field
                    v-model="fileName"
                    label="Názov práce"
                    outlined
                    dense
                ></v-text-field>

                <v-file-input
                    prepend-icon="mdi-upload"
                    v-model="file"
                    label="Vyberte súbor"
                    accept=".pdf,.doc,.docx"
                    outlined
                    dense
                ></v-file-input>

            </v-col>
            <v-col>

              <v-container>
                <v-radio-group v-model="selectedOption" row>
                  <v-radio label="Sekcia1" value="Sekcia1"></v-radio>
                  <v-radio label="Sekcia2" value="Sekcia2"></v-radio>
                  <v-radio label="Sekcia3" value="Sekcia3"></v-radio>
                </v-radio-group>

                <v-divider></v-divider>

                <p>Vybraná možnosť: {{ selectedOption }}</p>
              </v-container>

            </v-col>

            <v-col cols="12">
              <v-btn @click="uploadFile" block>
                Nahrať prácu
              </v-btn>
            </v-col>


          </v-row>

        </v-container>
      </v-form>
  </v-app>
</template>

<script>
export default {

  name : 'StudentView',
  data() {
    return {
      firstname: '',
      lastname: '',
      file: null,
      fileName: '',
      selectedOption: '',
    };
  },
  methods: {
    uploadFile() {
      if (this.file && this.fileName) {
        alert(`Súbor: ${this.fileName} bol úspešne nahratý!`);
        this.firstname = '';
        this.lastname = '';
        this.file = null;
        this.fileName = '';
        this.selectedOption = '';
      } else {
        alert('Prosím, vyberte súbor a zadajte názov práce.');
      }
    },
  },

}
</script>

<style scoped>

h1 {
  text-align: center;
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 20px;
  color: #3f51b5;
}

 .v-container {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}


.v-text-field,
.v-file-input {
  margin-bottom: 20px;
}


.v-btn {
  background-color: #3f51b5;
  color: white;
  font-weight: bold;
  font-size: 1rem;
  transition: background-color 0.3s ease;
}

.v-btn:hover {
  background-color: #283593;
}


.v-radio-group {
  margin-top: 20px;
}

.v-radio {
  --v-theme-primary: #3f51b5;
  margin-right: 10px;
}


.p {
  font-size: 1rem;
  color: #555;
  margin-top: 10px;
}


.v-divider {
  margin: 20px 0;
  border-color: #e0e0e0;
}


@media (max-width: 768px) {
  h1 {
    font-size: 2rem;
  }

  .v-container {
    padding: 15px;
  }

  .v-btn {
    font-size: 0.9rem;
  }
}
</style>