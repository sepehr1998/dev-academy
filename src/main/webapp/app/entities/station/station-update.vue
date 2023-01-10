<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="solitaApp.station.home.createOrEditLabel" data-cy="StationCreateUpdateHeading">Create or edit a Station</h2>
        <div>
          <div class="form-group" v-if="station.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="station.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="station-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="station-name"
              data-cy="name"
              :class="{ valid: !$v.station.name.$invalid, invalid: $v.station.name.$invalid }"
              v-model="$v.station.name.$model"
              required
            />
            <div v-if="$v.station.name.$anyDirty && $v.station.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.station.name.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="station-address">Address</label>
            <input
              type="text"
              class="form-control"
              name="address"
              id="station-address"
              data-cy="address"
              :class="{ valid: !$v.station.address.$invalid, invalid: $v.station.address.$invalid }"
              v-model="$v.station.address.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="station-startedFrom">Started From</label>
            <input
              type="number"
              class="form-control"
              name="startedFrom"
              id="station-startedFrom"
              data-cy="startedFrom"
              :class="{ valid: !$v.station.startedFrom.$invalid, invalid: $v.station.startedFrom.$invalid }"
              v-model.number="$v.station.startedFrom.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="station-endingIn">Ending In</label>
            <input
              type="number"
              class="form-control"
              name="endingIn"
              id="station-endingIn"
              data-cy="endingIn"
              :class="{ valid: !$v.station.endingIn.$invalid, invalid: $v.station.endingIn.$invalid }"
              v-model.number="$v.station.endingIn.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.station.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./station-update.component.ts"></script>
