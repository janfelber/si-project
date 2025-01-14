<template>
  <div>
    <div v-if="loading">Loading...</div>
    <div  style="display: flex; gap: 0.8rem;align-items: flex-start;">
      <div style="flex: 1;" class="review-card" v-if="articleAccepted || articleRejected">
        <table class="custom-table">
          <thead>
          <tr>
            <th>Oblasť</th>
            <th>Hodnotenie</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(detail, index) in reviewDetails" :key="index">
            <td><strong>{{ detail.columnName }}</strong></td>
            <td class="wrap-text">{{ detail.choiceName }}</td>
<!--            <td>{{ detail.text_value }}</td>-->
          </tr>
          </tbody>
        </table>
      </div>


      <div style="flex-direction: column">
        <div style="flex: 1; " class="article-info-card" v-if="articleAccepted || articleRejected" >
          <div class="card">

            <h2>Informácie o práci</h2>

            <div class="card-content">
              <p><strong>Názov práce:</strong> {{ articleName }}</p>
              <v-divider></v-divider>
              <p><strong>Meno:</strong> {{ firstName }}</p>
              <v-divider></v-divider>
              <p><strong>Priezvisko:</strong> {{ lastName }}</p>
              <v-divider></v-divider>
              <p><strong>Kľúčové slová:</strong> {{ articleKeyWords }}</p>
              <v-divider></v-divider>
              <p><strong>Spoluautori:</strong> {{ articleCoAuthors }}</p>
              <v-divider></v-divider>
              <p><strong>Kategória:</strong> {{ articleSection }}</p>
              <v-divider></v-divider>
            </div>
            <div class="footer">
              <span v-if="articleStatus === 'ACCEPTED'" class="status accepted">Akceptované</span>
              <span v-if="articleStatus === 'REJECTED'" class="status rejected">Zamietnuté</span>
              <span v-if="articleStatus === 'SENT'" class="status sent">Poslane</span>
            </div>
          </div>


        </div>
        <div v-if="articleRejected" style="margin-top: 1rem;">
          <btn class="button" @click="sendUserToUpload(this.conference_id)">Znova vložiť prácu</btn>
        </div>
      </div>






    </div>


  </div>

  <div v-if="articleInReview" style="display: flex; justify-content: center; align-items: center; height: 75vh;">
    <h1>Vaša práca je momentálne v procese hodnotenia.</h1>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ArticleReviewResponse',
  props: ['id'],
  data() {
    return {
      reviewDetails: [],
      articleName: "",
      articleStatus: "",
      articleKeyWords: "",
      articleCoAuthors: "",
      articleSection: "",
      firstName: "",
      lastName: "",
      articleInReview: null,
      articleAccepted: null,
      articleRejected: null,
      conference_id: null,
      review_id: null,
      loading: true
    }
  },
  computed: {
  },
  methods: {
    async sendUserToUpload(conferenceId) {
      this.$router.push({ name: 'upload', params: { id: conferenceId }});
    },
    async getReview() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(`http://localhost:8080/api/v1/review/getReviewByArticleId/${this.id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });

        console.log(response.data)
        this.review_id = response.data.reviewId
        console.log("review id", this.review_id)
        this.reviewDetails = response.data.reviewDetails
      } catch (error) {
        console.error(error);
      } finally {
        this.loading = false;  // Set loading to false when data is fetched
      }
    },
    async getArticle() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(`http://localhost:8080/api/v1/article/student/${this.id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
        // this.articles = response.data;
        console.log("get article",response.data)
        // this.reviewDetails = response.data.reviewDetails
        this.articleName = response.data.articleName
        this.articleStatus = response.data.status
        this.articleKeyWords = response.data.keyWords
        this.articleCoAuthors = response.data.coAuthors
        this.articleSection = response.data.section
        this.firstName = response.data.firstName
        this.lastName = response.data.lastName
        this.conference_id = response.data.conferenceId
      } catch (error) {
        console.error(error);
      }
    },
    async checkIfArticleIsInReview() {
      try {
        const token = localStorage.getItem("token");

        const response = await axios.get(
            `http://localhost:8080/api/v1/article/status/${this.id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        )

        if (this.articleStatus === "SENT") {
          this.articleInReview = true;
        } else if (this.articleStatus === "REJECTED") {
          this.articleRejected = true;
        } else if (this.articleStatus === "ACCEPTED") {
          this.articleAccepted = true;
        } else {
          this.articleInReview = false;
        }

      } catch (error) {
        console.error("Error checking article status:", error);
      }
    },

  },
  mounted() {
    this.getReview();
    this.checkIfArticleIsInReview();
    this.getArticle();
  },
}
</script>
<style scoped>

.button {
  background-color: #1EB386;
  border-color: #1EB386;
  font-size: 16px;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  text-align: center;
  text-decoration: none;
  margin-left: 20px;
}

.article-info-card .card {
  background-color: white;
  border: none;
  margin-top: 10px;

}

.article-info-card .card-header h2 {
  margin: 0;
  font-size: 20px;
}

.article-info-card .card-content p {
  margin: 10px 0;
  color: #333;
  font-size: 16px;
}


.article-info-card .status {
  font-weight: bold;
  font-size: 20px;
  padding: 5px 10px;
  border-radius: 5px;
}

.article-info-card .status.accepted {
  color: white;
  background-color: #28a745;
}

.article-info-card .status.rejected {
  color: white;
  background-color: #dc3545;
}

.article-info-card .status.sent {
  color: white;
  background-color: #ffc107;
}


.wrap-text {
  word-wrap: break-word;
  overflow-wrap: break-word;
  white-space: pre-wrap;
  max-width: 300px;
}

.custom-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 16px;
  text-align: left;
}

.custom-table th,
.custom-table td {
  border: 1px solid #d8d8f0;
  padding: 12px 15px;
}

.custom-table th {
  background-color: #f4f4f4;
  font-weight: bold;
  color: #333;
}


.review-card {
  max-height: fit-content;
  max-width: fit-content;
  border:     1px solid #d8d8f0;
  padding: 20px;
  border-radius: .5rem;
  background-color: #ffffff;
  margin-bottom: 10px;
  margin-left: 20px;
  margin-top: 10px;
}

.article-info-card {
  max-height: fit-content;
  width: 150%;
  min-width: 100%;
  border:     1px solid #d8d8f0;
  padding: 20px;
  border-radius: .5rem;
  background-color: #ffffff;
  margin-bottom: 10px;
  margin-left: 20px;
  margin-top: 10px;
}

</style>
