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
          <div class="form-group">
            <label>Reviewer</label>
            <div class="form-input">
              <select v-model="selectedReviewer">
                <option v-for="reviewer in reviewers" :key="reviewer.id" :value="reviewer.id" :selected="reviewer.id === selectedReviewer">
                  {{ reviewer.firstName }} {{ reviewer.lastName }}
                </option>
              </select>
            </div>
          </div>
        </div>
      </div>
      <div class="button-section">
        <button type="button" class="btn btn-primary" @click="updateArticle()" style="color: white">Uložiť</button>
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
      reviewers: [],
      article: [],
      articleName: '',
      articleDescription: '',
      keyWords: '',
      coAuthors: '',
      section: '',
      conferenceName: '',
      selectedReviewer: '',
      articleStatus: '',
      firstName: '',
      lastName: '',
      email: '',
      reviewerFirstName:'',
      reviewerLastName: '',
      reviewerFullName: '',
      reviewerId: null,
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
        this.selectedReviewer = this.article.reviewerId;
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
    async getAllReviewers() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(`http://localhost:8080/api/v1/user/available-reviewers/${this.id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
        this.reviewers = response.data;
        this.reviewers.forEach(reviewer => {
          this.reviewerFullName = `${reviewer.firstName} ${reviewer.lastName}`;
        });
      } catch (error) {
        console.error("Failed to fetch reviewers:", error);
      }
    },
    async updateArticle() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.put(`http://localhost:8080/api/v1/article/admin/update/${this.id}`, {
          reviewerId: this.selectedReviewer
        }, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        console.log('Article updated:', response.data);
      } catch (error) {
        console.error('Failed to update reviewer:', error);
      }
    }
  },
  mounted: async function() {
    try {
      await this.getUserData();
      await this.getAuthorInformation();
      await this.getAllReviewers();
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

.form-input select {
  border:     1px solid #d8d8f0;
  padding:    8px;
  width:      100%;
  box-sizing: border-box;
  appearance: none;
  background: url("data:image/svg+xml,%3Csvg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 16 16\"%3E%3Cpath d=\"M4.293 5.293a1 1 0 0 1 1.414 0L8 7.586l2.293-2.293a1 1 0 0 1 1.414 1.414L8 10.414l-3.707-3.707a1 1 0 0 1 0-1.414z\"%3E%3C/path%3E%3C/svg%3E") no-repeat right 0.75rem center;
  background-size: 8px 8px;
  cursor: pointer;
}

label {
  width:      10rem;
  align-self: center;
}

</style>