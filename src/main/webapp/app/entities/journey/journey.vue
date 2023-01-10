<template>
  <div>
    <h2 id="page-heading" data-cy="JourneyHeading">
      <span id="journey-heading">Journeys</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'JourneyCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-journey"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Journey </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && journeys && journeys.length === 0">
      <span>No journeys found</span>
    </div>
    <div class="table-responsive" v-if="journeys && journeys.length > 0">
      <table class="table table-striped" aria-describedby="journeys">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('distance')">
              <span>Distance</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'distance'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('duration')">
              <span>Duration</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'duration'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('departureStation.name')">
              <span>Departure Station</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'departureStation.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('returnStation.name')">
              <span>Return Station</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'returnStation.name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="journey in journeys" :key="journey.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'JourneyView', params: { journeyId: journey.id } }">{{ journey.id }}</router-link>
            </td>
            <td>{{ journey.distance }}</td>
            <td>{{ journey.duration | duration }}</td>
            <td>
              <div v-if="journey.departureStation">
                <router-link :to="{ name: 'StationView', params: { stationId: journey.departureStation.id } }">{{
                  journey.departureStation.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="journey.returnStation">
                <router-link :to="{ name: 'StationView', params: { stationId: journey.returnStation.id } }">{{
                  journey.returnStation.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'JourneyView', params: { journeyId: journey.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'JourneyEdit', params: { journeyId: journey.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(journey)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="solitaApp.journey.delete.question" data-cy="journeyDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-journey-heading">Are you sure you want to delete this Journey?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-journey"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeJourney()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="journeys && journeys.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./journey.component.ts"></script>
