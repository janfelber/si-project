<template >
      <v-app v-if="userInConference === true">
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

              <v-text-field
                  v-model="coAuthors"
                  label="coAthors"
                  outlined
                  dense
              ></v-text-field>

              <v-text-field
                  v-model="articleDescription"
                  label="articleDescription"
                  outlined
                  dense
              ></v-text-field>

              <v-text-field
                  v-model="keyWords"
                  label="keyWords"
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
              <v-btn @click="uploadFile()" block>
                Nahrať prácu
              </v-btn>
            </v-col>


          </v-row>

        </v-container>
      </v-form>
  </v-app>
</template>

<script>
import axios from 'axios';
import { th, tr } from 'vuetify/locale';

export default {

  name : 'UploadArticle',
  computed: {
    tr() {
      return tr
    }
  },
  props: ['id'],
  data() {
    return {
      firstname: '',
      lastname: '',
      coAuthors: '',
      articleDescription: '',
      keyWords: '',
      file: null,
      fileName: '',
      selectedOption: '',
      conferenceId: this.id,
      userInConference: null,
      articleInReview: null,
    };
  },
  methods: {
    async getUser() {
      try {
        const token = localStorage.getItem("token");

        const response = await axios.get(
            `http://localhost:8080/api/v1/user/me`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        this.userId = response.data.id;
        console.log(this.userId);
      } catch (error) {
        console.error("Error checking user in conference:", error);
      }
    },
    async checkIfArticleIsInReview() {
      try {
        const token = localStorage.getItem("token");

        const response = await axios.get(
            `http://localhost:8080/api/v1/article/status/${this.conferenceId}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        const status = response.data;

        if (status === "ACCEPTED") {
          this.articleInReview = true; // Prijatý článok
          this.articleStatusMessage = "Your article has been accepted.";
          console.log(this.articleInReview);
        } else if (status === "REJECTED") {
          this.articleInReview = false; // Zamietnutý článok
          this.articleStatusMessage = "Your article has been rejected.";
        } else if (status === "In Review") {
          this.articleInReview = true; // Článok je v recenzii
          this.articleStatusMessage = "Your article is in review.";
        } else {
          this.articleInReview = false;
          this.articleStatusMessage = "Article status is unknown.";
        }

      } catch (error) {
        console.error("Error checking article status:", error);
      }
    },
    async checkIfUserInConference() {
      try {
        const token = localStorage.getItem("token");
        const conferenceId = this.id;

        const response = await axios.get(
            `http://localhost:8080/api/v1/conference/isUserInConference?conferenceId=${conferenceId}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        if (response.data === "User is in the conference") {
          this.userInConference = true;
        } else {
          this.userInConference = false;
          this.$router.push({ name: 'activeConferences', params: { id: this.id }});
        }
      } catch (error) {
        console.error("Error checking user in conference:", error);
      }
    },
    async uploadFile() {
      if (!this.firstname || !this.lastname || !this.fileName || !this.selectedOption || !this.coAuthors || !this.articleDescription || !this.keyWords) {
        alert('Prosím, vyplňte všetky údaje');
        return;
      }

      if(!this.file) {
        alert('Prosím, vyberte súbor.');
        return;
      }

      try{
        const formData = new FormData();
        formData.append('file', this.file);
        formData.append('fileName', this.fileName);
        formData.append('coAuthors', this.coAuthors);
        formData.append('articleDescription', this.articleDescription);
        formData.append('keyWords', this.keyWords);
        formData.append('section', this.selectedOption);
        formData.append('firstName', this.firstname);
        formData.append('lastName', this.lastname);
        formData.append('conferenceId', this.conferenceId);

        const token = localStorage.getItem("token")
        console.log(localStorage.getItem("token"));
        const response = await axios.post("http://localhost:8080/api/v1/file/upload",formData,
            {
              headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'multipart/form-data'
              }
            });

        if (response.status === 200) {
          alert('Súbor bol úspešne nahratý!');
          this.firstname = '';
          this.lastname = '';
          this.file = null;
          this.fileName = '';
          this.selectedOption = '';
          this.coAuthors = '';
          this.articleDescription = '';
          this.keyWords = '';
        }
      }catch (error) {
        console.error("Chyba pri nahrávaní súboru", error);
        alert('Došlo k chybe pri nahrávaní súboru.');

      }
    },
  },
  mounted() {
    this.getUser();
    this.checkIfArticleIsInReview()
    this.checkIfUserInConference();
    console.log(this.userId);
  }
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