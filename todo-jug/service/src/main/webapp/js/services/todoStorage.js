/*global define*/
'use strict';

/**
 * Services that persists and retrieves TODOs from localStorage.
 */
define(['app'], function (app) {
	app.factory('todoStorage',['$http', function ($http) {

		var getAllTodos = function() {
            return $http.get('services/all');
        };

        var pushNewTodo = function(newTodo) {
            return $http.post('services/put', newTodo);
        };

        var deleteTodo = function(todo) {
            $http.delete('services/'+todo.id);
        }

        var updateTodo = function(todo) {
            return $http.post('services/update', todo);
        }

        var markAll = function(newStatus) {
            return $http.post('services/markAll/'+newStatus);
        }

		return {
			get: function (callback) {
				getAllTodos().success(function(data) {
                    console.log(data)
                    callback(data);
                });
			},

			put: function (newTodo, callback) {
				pushNewTodo(newTodo).success(function(data) {
				    console.log(data)
				    callback(data);
				});
			},
			remove: function (todo) {
			    deleteTodo(todo);
			},
			update: function (todo) {
			    updateTodo(todo);
			},
			markAll: function(newStatus, callback) {
			    markAll(newStatus).success(function(data) {
			        console.log(data)
			        callback(data);
			    });
			}
		};
	}]);
});
