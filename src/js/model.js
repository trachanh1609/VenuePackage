var model = {
  data: {
    rooms: [],
    packages: []
  }
}

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
