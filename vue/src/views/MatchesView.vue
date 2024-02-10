<template>
  <div>
    <h1>Matches View</h1>
    <MatchesListComp />
  </div>
</template>

<script>
import MatchesListComp from "../components/MatchesListComp"; // Adjust the path as needed

export default {
  name: "MatchesView",
  components: {
    MatchesListComp
  }
};
</script>

<style scoped>

</style>
