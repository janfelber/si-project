<template>
  <div>
    <div style="display: flex; gap: 0.8rem; align-items: flex-start;">
      <div style="flex: 1;" class="review-card" v-if="articleAccepted || articleRejected">
        <v-simple-table  dense>
          <tbody>
          <tr v-for="(detail, index) in reviewDetails" :key="index">
            <td><strong>{{ detail.columnName }} </strong></td>
            <td>{{ detail.choiceName }}</td>
            <td>{{ detail.text_value }}</td>
          </tr>
          </tbody>
        </v-simple-table>


      </div>

      <div style="flex: 1;" class="article-info-card" v-if="articleAccepted || articleRejected">
        <h2>Názov: {{ articleName }}</h2>
        <h2>Meno: {{ firstName }}</h2>
        <h2>Priezvisko: {{ lastName }}</h2>
        <h2>Kľúčové slová: {{ articleKeyWords }}</h2>
        <h2>Spoluautori: {{ articleCoAuthors }}</h2>
        <h2>Sekcia: {{ articleSection }}</h2>

        <div v-if="articleStatus === 'ACCEPTED'">
          <span v-if="articleStatus === 'ACCEPTED'" style="color: green;font-size: 30px;">Accepted</span>
        </div>

        <div v-if="articleStatus === 'REJECTED'">
          <span v-if="articleStatus === 'REJECTED'" style="color: red;font-size: 30px">Rejected</span>
        </div>

        <div v-if="articleStatus === 'SENT'">
          <span v-if="articleStatus === 'SENT'" style="color: orange;font-size: 30px">Sent</span>
        </div>

      </div>
    </div>

    <!-- Tlačidlo pre zamietnutý článok -->
    <div v-if="articleRejected" style="margin-top: 1rem;">
      <v-btn color="primary">Znova vložiť prácu</v-btn>
    </div>
  </div>

  <div v-if="articleInReview" style="display: flex; justify-content: center; align-items: center; height: 75vh;">
    <h1>Článok sa posudzuje</h1>
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
    }
  },
  computed: {
    groupedColumns() {
      const grouped = {};
      this.columns.forEach((column) => {
        if (!grouped[column.category_id]) {
          grouped[column.category_id] = {
            name: column.category_name,
            columns: []
          };
        }
        grouped[column.category_id].columns.push(column);
      });
      return grouped;
    }
  },
  methods: {
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
        this.reviewDetails = response.data.reviewDetails
      } catch (error) {
        console.error(error);
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
    this.checkIfArticleIsInReview();
    this.getReview();
    this.getArticle();
  },
}
</script>
<style scoped>


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
  max-width: 25%;
  border:     1px solid #d8d8f0;
  padding: 20px;
  border-radius: .5rem;
  background-color: #ffffff;
  margin-bottom: 10px;
  margin-left: 20px;
  margin-top: 10px;
}

v-simple-table {
  border-radius:    4px;
}

v-simple-table th {
  text-align: left;
  padding: 10px;
  background-color: #f4f4f4;
  border-bottom: 2px solid #ddd;
}

v-simple-table td {
  padding: 10px;
  border-bottom: 1px solid #eee;
}
</style>
