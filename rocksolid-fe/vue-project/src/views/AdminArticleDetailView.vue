<template>
  <div class="card-container">
    <div class="v-col-7">
      <div class="card">
        <v-card-title class="card-title">O praci
          <i class="fa fa-download" style="cursor: pointer; float: right;"></i>
        </v-card-title>
        <div class="card-body">
          <div class="form-group">
            <label>Nazov prace</label>
            <div class="form-input">
              <input v-model="articleName">
            </div>
          </div>
          <div class="form-group">
            <label>Popis</label>
            <div class="form-input">
              <input v-model="articleDescription">
            </div>
          </div>
          <hr>
          <div class="form-group">
            <label>Klucove slova</label>
            <div class="form-input">
              <input v-model="keyWords">
            </div>
          </div>
          <div class="form-group">
            <label>Spolu autori</label>
            <div class="form-input">
              <input v-model="coAuthors">
            </div>
          </div>
          <div class="form-group">
            <label>Sekcia</label>
            <div class="form-input">
              <input v-model="section">
            </div>
          </div>
          <div class="form-group">
            <label>Konferencia</label>
            <div class="form-input">
              <input v-model="conferenceName">
            </div>
          </div>
          <div class="form-group">
            <label>Status</label>
            <div class="form-input">
              <input v-model="articleStatus">
            </div>
          </div>
        </div>
      </div>
      <div class="button-section">
        <button type="button" class="btn btn-primary" @click="updateUser()" style="color: white">Uložiť</button>
      </div>
    </div>
    <div class="v-col-7">
      <div class="card">
        <v-card-title class="card-title">Autor</v-card-title>
        <div class="card-body">
          <div class="form-group">
            <label>Meno</label>
            <div class="form-input">
              <input v-model="articleName">
            </div>
          </div>
          <div class="form-group">
            <label>Priezvisko</label>
            <div class="form-input">
              <input v-model="lastName">
            </div>
          </div>
          <div class="form-group">
            <label>E-mail</label>
            <div class="form-input">
              <input v-model="email">
            </div>
          </div>
        </div>
      </div>
    </div>
    <div>
    </div>
  </div>
</template>


<script>
import axios from 'axios';

export default {
  props: ['id'],
  name: 'EditProfileView',
  data() {
    return {
      id_user: null,
      article: [],
      articleName: '',
      articleDescription: '',
      keyWords: '',
      coAuthors: '',
      section: '',
      conferenceName: '',
      articleStatus: '',
      firstName: '',
      lastName: '',
      email: '',
    };
  },
  methods: {
    async getUserData() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:8080/api/v1/article/' + this.id,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
        console.log(response.data);
        this.article = response.data;
        this.id_user = this.article.userId;
        this.articleName = this.article.articleName;
        this.articleDescription = this.article.articleDescription;
        this.keyWords = this.article.keyWords;
        this.coAuthors = this.article.coAuthors;
        this.section = this.article.section;
        this.conferenceName = this.article.conferenceName;
        this.firstName = this.article.firstName;
        this.lastName = this.article.lastName;
        this.articleStatus = this.article.status;
      } catch (error) {
        console.error('Failed to fetch user:', error);
      }
    },
    async getAuthorInformation() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/v1/user/" + this.id_user,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
        console.log("author information", response.data);
        this.user_data = response.data;
        this.email = this.user_data.email;
        this.isReviewer = this.user_data.role === "REVIEWER";
      } catch (error) {
        console.error("Failed to fetch user:", error);
      }
    },
  },
  mounted: async function() {
    try {
      await this.getUserData();
      await this.getAuthorInformation();
    } catch (error) {
      console.error('Error during mounted lifecycle:', error.message);
    }
  }
};
</script>

<style scoped>

.card-title i {
  font-size: 1.25rem;
  color:     #1EB386;
}

.card-container {
  display: flex;
}

.v-col-7 {
  flex: 1;
  max-width: 48%;
}

.button-section {
  display:    flex;
  margin-top: 1rem;
}

.form-input input {
  border:     1px solid #d8d8f0;
  padding:    8px;
  width:      100%;
  box-sizing: border-box;
}

.form-input {
  flex: 1;
}

hr {
  border:        0;
  border-top:    1px solid darkgrey;
  margin-bottom: .75rem;
  margin-top:    .75rem;
}

.card {
  display:          flex;
  flex-direction:   column;
  min-width:        0;
  position:         relative;
  word-wrap:        break-word;
  background-clip:  initial;
  background-color: #fefefe;
  border:           1px solid #d8d8f0;
  border-radius:    .5rem;
}

.card-body {
  min-height: 1px;
  padding:    .75rem;
}

.form-group {
  display:       flex;
  flex-wrap:     wrap;
  align-items:   center;
  margin-bottom: .75rem;
}

.card-title {
  font-weight:   500;
  font-size:     17px;
  border-bottom: 1px solid #d8d8f0;
}

.user-item ::before {
  color:         inherit;
  content:       "›";
  float:         left;
  padding-right: .375rem;

}

label {
  width:      10rem;
  align-self: center;
}

</style>