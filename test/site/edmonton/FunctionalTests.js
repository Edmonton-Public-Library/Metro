const async = require(`async`);
const net = require(`net`);

const argv = require('minimist')(process.argv.slice(2), {
  default: {
    server: process.env.DEPLOY_ENV === 'prod' ? 'epl-olr.epl.ca' : 'epl-olr-dev.epl.ca',
    firstname: 'Balzac',
    lastname: 'Billy',
    pin: '12345',
    email: 'ilsadmins@epl.ca'
  }
});
// console.log(`argv=${JSON.stringify(argv)}`);

const help = function() {
  console.log(`
    --token <access token> --card <card number> --server <ILS server> --firstname <first name>
    --lastname <last name> --pin <pin> --email <email>

    --token is required
    This is the access token for API access with the server.

    --card is required
    This is the unique card Id for the account to be created.
    Tips: For epl-olr.epl.ca, you can get an unused card number from https://epl-olr.epl.ca:8125/get_id
    or https://epl-olr-dev.epl.ca:8125/get_id for dev.

    --server is optional, when not specified:
    It is default to epl-olr.epl.ca if DEPLOY_ENV environment variable is 'prod'; otherwise
    epl-olr-dev.epl.ca.

    --firstname is optional, when not specified, it is default to Balzac.

    --lastname is optional, when not specified, it is default to Billy.

    --pin is optional, when not specified, it is default to 12345.

    --email is optional, when not specified, it is default to ilsadmins@epl.ca

    e.g.
    node FunctionalTests --token xxxyyyzzz --server localhost --card 1234567890
    node FunctionalTests --token xxxyyyxxx --card 1234567890
    node FunctionalTests --token xxxyyyxxx --card 1234567890 --firstname John --lastname Smith
    `);
};

if (argv.h) {
  help();
  process.exit();
}

const invalidToken = !argv.token || !((typeof argv.token === 'number' && argv.token > 0) || (typeof argv.token === 'string' && argv.token.length))
const invalidCard = !argv.card || !((typeof argv.card === 'number' && argv.card > 0) || (typeof argv.card === 'string' && argv.card.length))
if (argv.h || invalidToken || invalidCard) {
  if (invalidToken) console.error('Missing token!!!');
  if (invalidCard) console.error('Missing card number!!!');
  help();
  process.exit(!argv.h);
}

const READY = 'XK0';
const HANG_UP = 'XX0';
const END = '\n';

const start = function() {

  const tasks = [];

  tasks.push((done) => {
    request({
      code: 'GET_STATUS',
      authorityToken: `${argv.token}`,
      userId: 'dummy',
      pin: 'dummy',
      customer: 'null'}, done);
  });

  tasks.push((done) => {
    const customer = {
      "ID": `${argv.card}`,
      "PIN": `${argv.pin}`,
      "PREFEREDNAME":`${argv.lastname}, ${argv.firstname}`,
      "STREET": "7th Floor 10235 101 Street Nw",
      "CITY": "Edmonton",
      "PROVINCE": "AB",
      "POSTALCODE": "H0P0H0",
      "SEX": "M",
      "EMAIL": argv.email,
      "PHONE": "780-496-4058",
      "DOB": "19760823",
      "PRIVILEGE_EXPIRES": "20201104",
      "RESERVED": "User DELINQUENT",
      "ALTERNATE_ID": "X",
      "ISVALID": "Y",
      "ISMINAGE": "Y",
      "ISRECIPROCAL": "N",
      "ISRESIDENT": "Y",
      "ISGOODSTANDING": "Y",
      "ISLOSTCARD": "N",
      "FIRSTNAME": argv.firstname,
      "LASTNAME": argv.lastname
    };

    request({
      code: 'CREATE_CUSTOMER',
      authorityToken: `${argv.token}`,
      userId: 'null',
      pin: 'null',
      customer: JSON.stringify(customer)
    }, done);
  });

  tasks.push((done) => {
    request({
      code: 'GET_CUSTOMER',
      authorityToken: `${argv.token}`,
      userId: `${argv.card}`,
      pin: `${argv.pin}`,
      customer: 'null'}, done);
  });

  async.series(tasks, (error, results) => {
    console.log(`Completed: error=${JSON.stringify(error)} results=${JSON.stringify(results)}`);
  });
}

const request = function(command, callback) {

  console.log(`\nREQUEST ${JSON.stringify(command)}\n`);

  const client = new net.Socket();
  client.connect(2004, argv.server, () => {
    console.log(`Connecting to ${argv.server}\n`);
  });

  client.on('data', (data) => {
    console.log(`Received: ${data}\n`);
    if (data.toString().startsWith(READY)) {
      client.write(`${JSON.stringify(command)}${END}`);
      client.write(`${HANG_UP}${END}`);
    }
  });

  client.on('close', () => {
    console.log(`Connection closed\n`);
    callback();
  });

  client.on('error', (error) => {
    console.log(`Error: ${error}\n`);
    callback(error);
  });
}

// set a delay to allow debugging through chrome
// setTimeout(() => {
  start();
// }, 10000);
