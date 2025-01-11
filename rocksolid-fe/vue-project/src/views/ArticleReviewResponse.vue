<template>
  <div>
    <h2>Nazov : {{this.articleName}}</h2>
    <h2>Status : {{this.articleStatus}}</h2>
    <h2>Klucove slova : {{this.articleKeyWords}}</h2>
    <h2>Spoluautori : {{this.articleCoAuthors}}</h2>
    <h2>Sekcia : {{this.articleSection}}</h2>
    <h2>Meno : {{this.firstName}}</h2>
    <h2>Priezvisko : {{this.lastName}}</h2>

<!--    <div v-if="articleAccepted === true">-->
    <h2>Detaily recenzie</h2>
    <div v-for="(detail, index) in reviewDetails" :key="index">
      <p><strong> {{ detail.columnName }}</strong></p>
      <p><strong></strong> {{ detail.choiceName }}</p>
      <p><strong></strong> {{ detail.text_value }}</p>
      <hr />
    </div>
<!--    </div>-->
    <div v-if="articleInReview === true">
      <h1>
        Clanok sa posudzuje
      </h1>
    </div>

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
        // this.articles = response.data;
        console.log(response.data)
        this.reviewDetails = response.data.reviewDetails
        // this.articleName = response.data.articleName
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
          this.articleInReview = false;
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