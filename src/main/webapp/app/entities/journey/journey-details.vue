<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="journey">
        <h2 class="jh-entity-heading" data-cy="journeyDetailsHeading"><span>Journey</span> {{ journey.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Distance</span>
          </dt>
          <dd>
            <span>{{ journey.distance }}</span>
          </dd>
          <dt>
            <span>Duration</span>
          </dt>
          <dd>
            <span>{{ journey.duration | duration }} ({{ journey.duration }})</span>
          </dd>
          <dt>
            <span>Departure Station</span>
          </dt>
          <dd>
            <div v-if="journey.departureStation">
              <router-link :to="{ name: 'StationView', params: { stationId: journey.departureStation.id } }">{{
                journey.departureStation.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Return Station</span>
          </dt>
          <dd>
            <div v-if="journey.returnStation">
              <router-link :to="{ name: 'StationView', params: { stationId: journey.returnStation.id } }">{{
                journey.returnStation.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="journey.id" :to="{ name: 'JourneyEdit', params: { journeyId: journey.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./journey-details.component.ts"></script>
