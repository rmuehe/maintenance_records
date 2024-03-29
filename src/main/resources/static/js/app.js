/**
 * users.html form submission
 * handles POST to AdminController at /change-log-level
 * */
document.getElementById('log-level-form').addEventListener('submit', function(event) {
event.preventDefault(); // Prevent form from submitting the traditional way

var logPackage = document.getElementById('logPackage').value;
var logLevel = document.getElementById('logLevel').value;

// Assemble the data in a format suitable for sending
var formData = new FormData();
formData.append('logPackage', logPackage);
formData.append('logLevel', logLevel);

// Send an asynchronous request to the server to change the log level
fetch('/change-log-level', {
method: 'POST',
body: formData
})
.then(response => response.text())
.then(text => alert('Log level changed successfully'))
.catch(error => console.error('Error changing log level:', error));
});

