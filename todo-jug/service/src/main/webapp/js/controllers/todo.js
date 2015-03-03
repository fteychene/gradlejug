/*global define*/
'use strict';

/**
 * The main controller for the app. The controller:
 * - retrieves and persist the model via the todoStorage service
 * - exposes the model to the template and provides event handlers
 */

define(['app', 'services/todoStorage'], function (app) {
	return app.controller('TodoController', ['$scope', '$location', 'todoStorage', 'filterFilter',
		function TodoController($scope, $location, todoStorage, filterFilter) {
			var todos;

			todoStorage.get(function(data) {
			    todos = $scope.todos = data;
			});

			$scope.newTodo = '';
			$scope.editedTodo = null;

			$scope.$watch('todos', function () {
                if (todos != null) {
                    $scope.remainingCount = filterFilter(todos, { completed: false }).length;
                    $scope.doneCount = todos.length - $scope.remainingCount;
                    $scope.allChecked = !$scope.remainingCount;
				}
			}, true);

			if ($location.path() === '') {
				$location.path('/');
			}

			$scope.location = $location;

			$scope.$watch('location.path()', function (path) {
				$scope.statusFilter = (path === '/active') ?
					{ completed: false } : (path === '/completed') ?
					{ completed: true } : null;
			});


			$scope.addTodo = function () {
				var newTodo = $scope.newTodo.trim();
				if (!newTodo.length) {
					return;
				}

                todoStorage.put({name: newTodo, completed: false }, function (data) {
				    todos.push(data);
				});

				$scope.newTodo = '';
			};


			$scope.editTodo = function (todo) {
				$scope.editedTodo = todo;
			};

			$scope.update = function (todo) {
			    todoStorage.update(todo);
			};


			$scope.doneEditing = function (todo) {
				$scope.editedTodo = null;
				todo.name = todo.name.trim();

				if (!todo.name) {
					$scope.removeTodo(todo);
				} else {
				    $scope.update(todo);
				}
			};


			$scope.removeTodo = function (todo) {
			    todoStorage.remove(todo);
                todos.splice(todos.indexOf(todo), 1);
			};


			$scope.clearDoneTodos = function () {
				$scope.todos = todos = todos.filter(function (val) {
				    if (val.completed) {
				        todoStorage.remove(val);
				    }
					return !val.completed;
				});
			};


			$scope.markAll = function (done) {
			    todoStorage.markAll(!done, function(data) {
			        todos = $scope.todos = data;
			    });
			};
		}
	]);
});
