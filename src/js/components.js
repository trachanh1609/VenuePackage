
Vue.component('booking', {

})

Vue.component('search-input-box', {
  template:'#search-input-box-template',
  data: function (){
    return {
      meetingDate: new Date(),
      meetingTime: '',
      meetingLengthOptions: meetingLengthOptions,
      meetingLength: '2',
      seatingOrderOptions: seatingOrderOptions,
      seatingOrder: '',
      persons: '10',
      coffee: false,
      food: false,
      accommodation: false
    }
  },
  methods: {
      handleChange: function(value) {
        // console.log(value)
      }
  }
})

Vue.component('search-result', {
  template: '#search-result-template'
})
//list of rooms

Vue.component('venue-brief', {

})
// Previous , Next Buttons
// Room or Package Component


Vue.component('result-items', {
  template: '#result-items-template',
  data: function () {
    return searchResult
  }

})

Vue.component('result-item', {
  template: '#result-item-template',
  props:['resultItem'],
  data: function () {
    var rData = {
      tabSelected: 'room',
      showRoom: false
    }
    return rData
  },
  methods: {
    switchRoomPackage: function(tabName) {
      this.tabSelected = tabName
    }
  }
})

Vue.component('room-photos', {
  template: '#room-photos-template',
  props: ['id']
})

Vue.component('room-brief', {
  template: '#room-brief-template'
})

Vue.component('packages', {
  template: '#packages-template',
  props: ['id']
})

Vue.component('package-brief', {
  template: '#package-brief-template'
})
