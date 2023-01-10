<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="station">
        <h2 class="jh-entity-heading" data-cy="stationDetailsHeading"><span>Station</span> {{ station.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ station.name }}</span>
          </dd>
          <dt>
            <span>Address</span>
          </dt>
          <dd>
            <span>{{ station.address }}</span>
          </dd>
          <dt>
            <span>Started From</span>
          </dt>
          <dd>
            <span>{{ station.startedFrom }}</span>
          </dd>
          <dt>
            <span>Ending In</span>
          </dt>
          <dd>
            <span>{{ station.endingIn }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="station.id" :to="{ name: 'StationEdit', params: { stationId: station.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./station-details.component.ts"></script>
