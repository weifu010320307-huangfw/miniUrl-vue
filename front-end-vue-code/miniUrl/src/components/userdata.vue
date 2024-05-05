<script setup>
import DataTable from 'datatables.net-vue3';
import DataTablesCore from 'datatables.net-bs5';
import { onMounted, ref, render } from 'vue';
import $ from 'jquery'

DataTable.use(DataTablesCore);

const userdata = ref();
let dt;

// Get a DataTables API reference
onMounted(function () {


  dt = userdata.value.dt;


  
});

const options = {
  "processing": true,
  "serverSide": true
};

const columns = [
  { data: 'shortUrl', "width": "10%" },
  { data: 'longUrl', "width": "50%", render: renderFunction() },
  { data: 'shortenTimes', "width": "10%" },
  { data: 'accessTimes', "width": "10%" },
  { data: 'belongUser', "width": "10%" },
  
];



function renderFunction()
{
  let render = $().dataTable.render.text();
  
  console.log(render);
  return render;
}




const csrfToken = document.cookie.replace(
                    /(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/,
                    "$1",
                    );


const ajax = {
            "url": import.meta.env.VITE_SERVER_BASE_URL + "/userdata",
            "headers": {  
                "X-XSRF-TOKEN": csrfToken
            },
            "type": "POST",
            "dataType": "json",
            "contentType": "application/json",
            xhrFields: {
              withCredentials: true,
            },
            "data": function (d) {

              let requestData = JSON.stringify(d);
              console.log("requestData = " + requestData);
                return requestData;
            }
};



</script>

<template>
    <div class="container-fluid">
      <caption class="caption">
        <h1>Url shorten statistics for current user</h1>
      </caption>
      <!-- <h2>DataTables + Vue3 example</h2> -->


      <DataTable
        class="table table-hover table-striped"
        :ajax="ajax"
        :columns="columns"
        :options="options"
        width="100%"
        style="word-break: break-word;"
        role="table"
        ref="userdata"
      >
      <thead>
        <tr>
            <th scope="col">Short Url</th>
            <th scope="col">Long Url</th>
            <th scope="col">Shorten Times</th>
            <th scope="col">Access Times</th>
            <th scope="col">Belong User</th>

        </tr>
        </thead>
        <tfoot>
        <tr>
            <th scope="col">Short Url</th>
            <th scope="col">Long Url</th>
            <th scope="col">Shorten Times</th>
            <th scope="col">Access Times</th>
            <th scope="col">Belong User</th>
        </tr>
      </tfoot>
      </DataTable>
  </div>

</template>


<style>



@import 'bootstrap';
@import 'datatables.net-bs5';



.caption h1
{
  font-size: 2rem;
  font-style: italic;
  color: blue;
  font-weight: bold;
  white-space: nowrap;

}

.container-fluid
{
  padding-top: 2%;
  padding-right: 1%;
  padding-left: 1%;
}

</style>


