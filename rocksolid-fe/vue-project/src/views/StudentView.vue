
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

</style>