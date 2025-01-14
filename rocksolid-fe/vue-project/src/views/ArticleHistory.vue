<template>
  <v-app>
    <v-main>
      <div class="filter-header">
        <h4 style="font-size: 20px"> Filter </h4>
      </div>
      <div class="filter-container">
        <div class="form-group">
          <label>Vyhladavacia fraza</label>
          <div class="form-input">
            <input v-model="searchQuery" placeholder="Zadajte vyhľadávací výraz">
          </div>
        </div>
        <div class="form-group filter-button">
          <button class="btn btn-primary filter" @click="filter" style="color: white">Filter</button>
          <button class="btn btn-primary reset" @click="reset" style="color: #1EB386">Resetovat</button>
        </div>
      </div>

      <div class="articles" style="padding: 0 1.5rem">
        <v-row class="pa-4" align="stretch" justify="start">
          <v-col
              cols="12"
              sm="6"
              md="4"
              lg="3"
              v-for="article in filteredArticles"
              :key="article.id"
          >
            <v-card class="elevation-3" >
              <div class="card-header-green"></div>

              <v-card-title class="text-h6 font-weight-bold">
                {{ article.articleName }}
              </v-card-title>
              <v-card-subtitle class="text-body-2 grey--text">
                Nahrané: {{ dayjs(article.createdAt).format('D.M.YYYY') }}
              </v-card-subtitle>
              <v-divider></v-divider>
              <v-card-text class="py-3">
                <div class="text-truncate">
                  Konferencia: {{ article.conferenceName }}
                </div>
                <v-divider></v-divider>
                <div class="text-truncate">
                  Popis: {{ article.description }}
                </div>
                <v-divider></v-divider>
                <div class="text-truncate">
                  Kategória: {{ article.section }}
                </div>
                <v-divider></v-divider>
                <div class="text-truncate">
                  Kľúčové slová: {{ article.keywords }}
                </div>
                <v-divider></v-divider>
                <div class="text-truncate">

                  <div style="margin-left: -10px">
                    <div class="status">
                      <div v-if="article.status === 'ACCEPTED'">
                        <span v-if="article.status === 'ACCEPTED'" class="status accepted" >Akcteptované</span>
                      </div>

                      <div v-if="article.status === 'REJECTED'">
                        <span v-if="article.status === 'REJECTED'" class="status rejected">Zamietnuté</span>
                      </div>

                      <div v-if="article.status === 'SENT'">
                        <span v-if="article.status === 'SENT'" class="status sent">POSLANÉ</span>
                      </div>
                    </div>
                  </div>


                </div>
              </v-card-text>
              <v-card-actions>
                <v-row class="w-100" justify="space-between">
                  <v-col class="d-flex justify-right" style="margin-left: 7px">
                    <btn class="btn btn-primary review-button"
                        @click="showArticleReview(article.id)" v-if="article.status !== 'SENT'">
                      Pozrieť recenziu
                    </btn>
                  </v-col>

                  <v-col class="d-flex justify-end">
                    <v-btn
                        class="blue-darken-1 white--text"
                        @click="downloadArticle(article.id)"
                    >
                      <i class="fa fa-download" style="cursor: pointer;"></i>
                    </v-btn>
                  </v-col>

                </v-row>
              </v-card-actions>
            </v-card>

          </v-col>
        </v-row>
      </div>
    </v-main>
  </v-app>
</template>



<script>

import axios from 'axios';
import dayjs from 'dayjs';

export default {
name: "ArticleHistory",
  computed: {
    dayjs() {
      return dayjs
    }
  },
  data() {
    return {
      searchQuery: '',
      articles: [],
      filteredArticles: [],
      user_id: null,
      fileName: null
    };
  },
  mounted() {
    this.filteredArticles = this.articles;
    this.getUser();
  },
  methods: {
    filter() {
      this.filteredArticles = this.articles.filter(article => {
        return article.articleName.toLowerCase().includes(this.searchQuery.toLowerCase());
      });
    },
    async getUser(){
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/v1/user/current-user", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.user_id = response.data;
        await this.fetchAvailableArticles();
      } catch (error) {
        console.error("Failed to fetch user id", error);
      }
    },
    async fetchAvailableArticles() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/v1/article/user/" + this.user_id,
            {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.articles = response.data;
        this.filteredArticles = this.articles;
        this.conference_name = this.articles.conferenceName;
      } catch (error) {
        console.error("Failed to fetch articles", error);
        this.articles = [];
      }
    },
    showArticleReview(id){
      this.$router.push({ name: 'ArticleReviewResponse', params: { id: id } });
    },
    async getFileName(id){
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/v1/file/fileName/" + id,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            });
        this.fileName = response.data;
      } catch (error) {
        console.error("Failed to fetch article name", error);
      }
    },
    reset() {
      this.searchQuery = '';
      this.filteredArticles = this.articles;
    },
    downloadArticle(id){
      this.getFileName(id);
      let fileType;
      if(this.fileName != null){
        fileType = this.fileName.substring(this.fileName.lastIndexOf("."))
      }
      const token = localStorage.getItem("token");
      const url = "http://localhost:8080/api/v1/file/download/" + id;
      axios
          .get(url, {
            responseType: "blob",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
          .then((response) => {
            let type;
            switch (fileType) {
              case "pdf":
                type = 'application/pdf';
                break;
              case "docx":
                type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                break;
              case "doc":
                type = "application/msword";
                break;
            }
            const blob = new Blob([response.data], { type: type });
            const link = document.createElement("a");
            link.href = window.URL.createObjectURL(blob);
            link.download = this.fileName;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
          })
          .catch((error) => {
            console.error("File download failed:", error);
          });

    },
  }
};
</script>

<style scoped>


.status{
  font-weight: bold;
  font-size: 15px;
  padding: 5px 10px;
  border-radius: 5px;
}

.status.accepted {
  color: white;
  background-color: #28a745;
}

.status.rejected {
  color: white;
  background-color: #dc3545;
}

.status.sent {
  color: white;
  background-color: #ffc107;
}

.card-header-green {
  height: 5px;
  background-color: #4caf50;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
}
.v-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.v-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.15);
}
.v-btn {
  border-radius: 20px;
  text-transform: none;
}
.v-card-title {
  color: #1b5e20;
}

.v-card-text {
  margin-bottom: 1rem;
}

.v-card-text .text-truncate {
  font-size: 0.875rem;
  color: #555;
}

.filter-header {
  align-items:      center;
  border-bottom:    1px solid #d8d8f0;
  background-color: #f7f7fc;
  display:          flex;
  padding:          .75rem 2.5rem;

}

.filter-container {
  border-bottom: 1px solid #d8d8f0;
  padding: .75rem 2.5rem;
}

.form-group {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  margin-bottom: .75rem;
}

.form-input input {
  border: 1px solid #d8d8f0;
  padding: 8px;
  width: 40%;
  box-sizing: border-box;
  font-size: 0.75rem;
}

label {
  width: 11rem;
  align-self: center;
  font-size: 14px;
}

.form-input {
  flex: 1;
}

.filter-button {
  display: flex;
  gap: 10px;
  justify-content: flex-start;
  margin-top: 4rem;
}

.filter-button .filter {
  background-color: #1EB386;
  border-color: #1EB386;
}

.filter-button .reset {
  background-color: transparent;
  border-color: #1EB386;
}

.filter-button .filter,
.filter-button .reset {
  font-weight: bold;
  font-size: 14px;
  padding: 6px 12px;
}

.review {
  font-size: 15px;
  padding: 4px 8px;
  height: auto;
  width: auto;
  font-weight: bold;
  background-color: #1EB386;
  border-color: #1EB386;
}

.review-button {
  background-color: #1EB386;
  border-color: #1EB386;
  font-size: 14px;
}

.form-input input:focus {
  outline: none;
}
</style>