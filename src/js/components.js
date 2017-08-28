var searchResult =   {
    resultItems: [
      {
        id: 0,
        roomName: 'Daenerys Targaryen'
      },
      {
        id: 1,
        roomName: 'Sokos Classroom A'
      },
      {
        id: 2,
        roomName: 'Sokos Classroom B'
      },
      {
        id: 3,
        roomName: 'Sokos Classroom C'
      }
    ]
  }

var meetingLengthOptions = [{
          value: '2',
          label: '2h'
        }, {
          value: '3',
          label: '3h'
        }, {
          value: '4',
          label: '4h'
        }, {
          value: '5',
          label: '5h'
        }, {
          value: '6',
          label: '6h'
        },
        {
          value: '7',
          label: '7h'
        },
        {
          value: '8',
          label: '8h'
        },
        {
          value: '9',
          label: '9h'
        },
        {
          value: '10',
          label: '10h'
        },
        {
          value: '24',
          label: 'Overnight'
        },
        {
          value: '48',
          label: '2 Days'
        },
      ]

var seatingOrderOptions = [{
          value: 'U-shape',
          label: 'U-shape'
        }, {
          value: 'BoardRoom',
          label: 'BoardRoom'
        }, {
          value: 'ClassRoom',
          label: 'ClassRoom'
        }, {
          value: 'Banquet',
          label: 'Banquet'
        }, {
          value: 'Theatre',
          label: 'Theatre'
        }]

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
      handleChange(value) {
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
