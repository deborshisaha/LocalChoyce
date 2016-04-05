var ProductReview = Parse.Object.extend("ProductReview");
var Product = Parse.Object.extend("Product");
var UserProduct = Parse.Object.extend("UserProduct");
var User = Parse.Object.extend("_User");

Parse.Cloud.define("product", function(request, response) {

	var promises = [];
	var userObject = null;
	
	if (request.params.user) {
		userObject = new User();
		userObject.id = request.params.user;
	}

	var limit = Math.floor(Math.random() * 4) + 1 ;

	var productObject = new Product();
	productObject.id = request.params.product;
	
	var forPresentation = false;
	forPresentation = request.params.forPresentation;
	
	var productReviewQ = new Parse.Query(ProductReview);
	productReviewQ.include("user");
	
	if (!forPresentation) {
		console.log("Not for presentation" + request.params);
		productReviewQ.equalTo( "product", productObject );
	}
	
	productReviewQ.limit(limit);
	productReviewQ.ascending("createdAt");
	
	var suggestedItemQ = new Parse.Query(Product);
	suggestedItemQ.include("productPostedBy");
	suggestedItemQ.limit(5);
	suggestedItemQ.ascending("createdAt");
		
	// Push promises
	promises.push(productReviewQ.find());
	promises.push(suggestedItemQ.find());
	
	// Push favorite query
	if (userObject != null ) {
		console.log("User");
		var userLikedQuery = new Parse.Query(UserProduct);
		userLikedQuery.equalTo("userId", userObject);
		userLikedQuery.equalTo("productId", productObject);
		
		// Push favorite query
		promises.push(userLikedQuery.find());
		
		Parse.Promise.when(promises).then(function(productReviews, suggestedProducts, fav) {
			var isFavorite = false;
			console.log("fav length" + fav.length);
			if (fav !=null && fav.length == 1) {
				isFavorite = true;
			}
			var jsonObject = {
				"isFavorite": isFavorite,
				"reviews": productReviews,
				"suggested": suggestedProducts
			};
			response.success(jsonObject);
		}, function(error) {
	   		response.error(error);
		});
	} else {
		console.log("No user");
		Parse.Promise.when(promises).then(function (productReviews, suggestedProducts) {

			var jsonObject = {
				"isFavorite": false,
				"reviews": productReviews,
				"suggested": suggestedProducts
			};
	
    		response.success(jsonObject);
		}, function(error) {
			response.error(error);
		});
	}
});